console.log("board comment js in");
console.log(bnoVal);

document.getElementById('cmtPostBtn').addEventListener('click',()=>{
	const cmtText = document.getElementById('cmtText');
	if(cmtText.value == null || cmtText==""){
		alert("댓글 내용을 입력해주세요.");
		cmtText.focus();
		return false;
	}else{
		let cmtData={
			bno:bnoVal,
			writer:document.getElementById('cmtWriter').innerText,
			content : cmtText.value
		};
		//전송
		postCommentToServer(cmtData).then(result =>{
			if(result ==="1"){
				alert("댓글등록성공~!!");
			}
			//댓글 뿌리기
			spreadCommentList(bnoVal);
		})
	}
});

async function postCommentToServer(cmtData){
	try{
		const url ="/comment/post";
		const config={
			method:'post',
			headers:{
				'content-type':'application/json; charset=utf-8'
			},
			body: JSON.stringify(cmtData)
		};
		const resp = await fetch(url, config);
		const result = await resp.text();
		return result;
	}catch(error){
		console.log(error);
	}
};

async function getCommentListFromServer(bno,page){
	try{
		const resp = await fetch('/comment/list/'+bno+'/'+page);
		const result = await resp.json();
		return result;
	}catch(error){
		console.log(error);
	}
	
};

//페이징 처리를 하여, 한페이지(더보기)에 5개 댓글을 출력
//전체 게시글 수에 따른 페이지 수
function spreadCommentList(bno, page=1){
	getCommentListFromServer(bno, page).then(result =>{
		console.log(result);  //ph cmtList
		const ul = document.getElementById('cmtListArea');
		if(result.cmtList.length>0){
			if(page == 1){ //1page에서만 댓글 내용 지우기.
				ul.innerHTML =''; //ul에 원래있던 html 값 지우기
			}
			for(let cvo of result.cmtList){
				let li=`<li data-cno="${cvo.cno}" class="list-group-item">`;
				li +=`<div class="ms-2 me-auto">`;
				li +=`<div class="fw-bold">${cvo.writer}</div>`;
				li +=`${cvo.content}`;
				li +=`</div>`;
				li +=`<span class="badge bg-dark rounded-pill">${cvo.modAt}</span>`;
				li +=`<button type="button" class="btn btn-outline-warning mod" data-bs-toggle="modal" data-bs-target="#myModal">e</button>`;
				li +=`<button type="button" class="btn btn-outline-danger del">x</button>`;
				li +=`</li>`;
				ul.innerHTML += li;
			}
			//page 처리
			let moreBtn = document.getElementById('moreBtn');
			//현재 페이지 번호가 전체 페이지 번호보다 작다면
			//아직 나와야 할 페이지가 더 있다면...
			if(result.pgvo.pageNo < result.realEndPage){
				//숨김 속성 해지
				moreBtn.style.visibility = "visible"; //표시
				//페이지 +1
				moreBtn.dataset.page = page+1;
			}else{
				moreBtn.style.visibility = "hidden"; //숨김
			}
		}else{
			let li = `<li class="list-group-item">Comment List Empty</li>`;
			ul.innerHTML = li;
		}
	})
};

//더보기 버튼 작업
document.addEventListener('click',(e)=>{
    if(e.target.id == 'moreBtn'){
        let page = parseInt(e.target.dataset.page);
        spreadCommentList(bnoVal, page);
    }

    //수정 시 모달창을 통해 댓글 입력받기
    else if(e.target.classList.contains('mod')){
        //내가 수정버튼을 누른 댓글의 li
        let li = e.target.closest('li');  
        //writer 를 찾아서 id="modWriter" 에 넣기
        let modWriter = li.querySelector(".fw-bold").innerText;
        document.getElementById("modWriter").innerText = modWriter;

        //nextSibling : 한 부모 안에서 다음 형제를 찾기
        let cmtText = li.querySelector('.fw-bold').nextSibling;
        console.log(cmtText);
        document.getElementById('cmtTextMod').value = cmtText.nodeValue;

        //수정 => cno dataset으로 달기 cno, content
        document.getElementById('cmtModBtn').setAttribute("data-cno", li.dataset.cno);
    }
    else if(e.target.id == 'cmtModBtn'){
        let cmtModData = {
            cno: e.target.dataset.cno,
            content: document.getElementById('cmtTextMod').value
        }; 
        console.log(cmtModData);
        //비동기로 보내기
        editCommentToServer(cmtModData).then(result =>{
            if(result == "1"){
                alert("댓글 수정 성공");
                //모달창 닫기
                document.querySelector(".btn-close").click();
            }else{
                alert("수정실패");
                document.querySelector(".btn-close").click();
            }
            // 댓글 새로 뿌리기
            spreadCommentList(bnoVal);
        })
    }
    //삭제
    else if(e.target.classList.contains('del')){
        //cno
        //let cnoVal = e.target.dataset.cno;
        let li = e.target.closest('li');  
        let cnoVal = li.dataset.cno;
        //비동기로 삭제 요청
        removeCommentToServer(cnoVal).then(result =>{
            if(result == "1"){
                alert("댓글 삭제 성공~!!");
                spreadCommentList(bnoVal);
            }
        })
    }

});

async function removeCommentToServer(cnoVal){
    try {
        const url="/comment/remove/"+cnoVal;
        const config={
            method:"delete"
        };
        const resp = await fetch(url, config);
        const result = await resp.text();
        return result;
    } catch (error) {
        console.log(error);
    }
}


async function editCommentToServer(cmtModData){
    try {
        const url = "/comment/edit";
        const config={
            method:"put",
            headers:{
                'content-type':'application/json; charset=utf-8'
            },
            body: JSON.stringify(cmtModData)
        }
        const resp = await fetch(url, config);
        const result = await resp.text();
        return result;
    } catch (error) {
        console.log(error);
    }
}
