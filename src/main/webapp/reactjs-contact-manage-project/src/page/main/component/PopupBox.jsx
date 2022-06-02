import {getData, postData, putData} from "../../../service/Fetch";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";


function regAnswer(updateData,setStates){
    const URL=`/api/answers/${updateData.id}`;
    const {setLoadData} = setStates;

    const callbackFunc = {
        callbackOk() {
            alert('정상 처리되었습니다.');
            setLoadData(prev=>{return {...prev}});
        }
    }

    if(updateData==null ) {
        alert('등록할 대상이 없습니다.');
        return false;
    }
    if(updateData.id ==null){
        alert('[answerId(답변번호]]는 필수입니다.');
        return false;
    }
    if(updateData.userId ==null){
        alert('[userId(사용자번호]]는 필수입니다.');
        return false;
    }
    if(updateData.inquiryId ==null){
        alert('[inquiryId(질문번호]]는 필수입니다.');
        return false;
    }
    if(updateData.title ==null || updateData.title.length<4){
        alert('답변 제목을 4자 이상 입력해주세요.');
        return false;
    }
    if(updateData.content ==null || updateData.content.length<10){
        alert('답변 내용을 10자 이상 입력해주세요.');
        return false;
    }
    if(updateData.isFinished !=='Y'){
        alert('[isFinished]는 Y여야 합니다.');
        return false;
    }

    putData(URL, updateData, callbackFunc);
}


/**
 * 접수 처리
 */
function doAcceptCheckedList(acceptData,setStates){
    const {setLoadData} = setStates;

    const callbackFunc = {
        callbackOk() {
            alert('정상 처리되었습니다.');
            setLoadData(null);
        }
    }

    if(acceptData==null ) {
        alert('접수할 대상이 없습니다.');
        return false;
    }

    if(acceptData.userId == null || acceptData.inquiryId == null || acceptData.isFinished !=='N'){
        console.error('userId',acceptData.userId);
        console.error('inquiryId',acceptData.inquiryId);
        console.error('isFinished',acceptData.isFinished);
        alert('접수 대상 정보가 정상적으로 설정되지 않았습니다.');

        return true;
    };

    postData('/api/answers', [acceptData], callbackFunc);
}

/**
 * 문의/답변 불러오기
 */
function loadData({inquiryId,loadedData}, {setLoadData,setErrorHttpStatus, setAnsweredData}){

    if(inquiryId==null){
        alert('[inquiryId(문의id)] 정보가 없습니다.');
        return;
    }
    let URL, callbackFunc;
    if(loadedData!=null && loadedData.answerId!=null) {
        URL = '/api/answers/' + loadedData.answerId;
        callbackFunc = {
            callbackOk(data){
                setAnsweredData({...data});
            },
            setErrorHttpStatus : setErrorHttpStatus};
    }
    else if(loadedData==null){
        console.log('!!!!!!!')
        URL = '/api/inquiries/' + inquiryId;
        callbackFunc = {
            callbackOk(data){
                setLoadData({...data});
            },
            setErrorHttpStatus : setErrorHttpStatus};
    }

    getData(URL, null,callbackFunc);
}


function PopupBox({inquiryId, setInquiryId}){

    const [errorHttpStatus, setErrorHttpStatus] = useState(0);
    const [loadedData, setLoadData] = useState(null);
    const [loadedAnsweredData, setAnsweredData] = useState(null);
    const [textAreaValue, setTextAreaValue] = useState(null);
    const [inputTextValue, setInputTextValue] = useState(null);
    const navigate = useNavigate();

    const states = {inquiryId,errorHttpStatus, loadedData, loadedAnsweredData};
    const setStates = {setInquiryId,setErrorHttpStatus, setLoadData, setAnsweredData};

    useEffect(()=>{
        loadData(states, setStates);
    },[loadedData]);
    useEffect(()=>{
        if(errorHttpStatus>0) {
            if (errorHttpStatus === 401)
                navigate('/');
            else {
                alert('처리 중 오류가 발생했습니다.');
            }
        }
    }, [errorHttpStatus]);


    return(
      <div className={"dimmed-area"}>
          <div className={"popup-wrapper"}>
              <div className={"inquiry-area mt-20"}>
                  <div className={"title-row"}>
                      <h2>{loadedData!=null?loadedData.title:null}</h2>
                  </div>
                  <div className={"row mt-25"}>
                      <label>아이디</label>
                      <span>{loadedData!=null?loadedData.customerId:null}</span>
                  </div>
                  <div className={"row mt-10"}>
                      <label>작성일시</label>
                      <span>{loadedData!=null?loadedData.createdDateTime:null}</span>
                  </div>
                  <div className={"row content-row mt-10"}>
                      <label>문의내용</label>
                      <div className={"content-area"}>{loadedData!=null?loadedData.content:null}
                      </div>
                  </div>
              </div>
              <div className={"text-align-right"}>
                  <span className={"comment mr-10"}>*접수한 후에 답변을 등록할 수 있습니다.</span>
                <button className={"btn upd-btn mt-20"}
                        onClick={()=>doAcceptCheckedList(
                            {'userId':loadedData.loginInfo.id, 'inquiryId':loadedData.id, 'isFinished':'N'}
                        , setStates)}
                        disabled={loadedData==null||loadedData.answered||loadedData.answerId!=null}>접수</button>
              </div>
              <div className={"answer-area mt-5"}>
                  <div className={"row mt-25"}>
                      <label>상담사</label>
                      <input type={"text"} disabled={"true"}
                             value={loadedAnsweredData!=null? `${loadedAnsweredData.user.userNm}(${loadedAnsweredData.user.userEmailAddr})`:null}></input>
                      <label>접수일시</label>
                      <input type={"text"} disabled={"true"} value={loadedAnsweredData!=null?loadedAnsweredData.createdDateTime:null}></input>
                  </div>
                  <div className={"row mt-10"}>
                      <label>답변일시</label>
                      <input type={"text"} disabled={"true"} value={loadedAnsweredData!=null?loadedAnsweredData.finishedDateTime:null}></input>
                      <label>답변여부</label>
                      <input type={"text"} disabled={"true"} value={loadedAnsweredData!=null?loadedAnsweredData.isFinished:null}></input>
                  </div>
                  <div className={"row mt-10"}>
                      <label>제목</label>
                      <input type={"text"} placeholder={"제목을 입력하세요"} value={loadedAnsweredData!=null?loadedAnsweredData.title:null}
                             onChange={e=>setInputTextValue(e.target.value)}
                             disabled={loadedAnsweredData==null||loadedAnsweredData.isFinished==='Y'||
                             loadedAnsweredData.user.id!=loadedData.loginInfo.id}></input>
                  </div>
                  <div className={"row mt-10"}>
                      <label>답변</label>
                      <textarea rows={"5"} cols={"20"} wrap={"hard"}
                                placeholder={"내용을 입력하세요"}
                                onChange={e=>setTextAreaValue(e.target.value)}
                                value={loadedAnsweredData!=null?loadedAnsweredData.content:null}
                                disabled={loadedAnsweredData==null||loadedAnsweredData.isFinished==='Y'||
                                loadedAnsweredData.user.id!=loadedData.loginInfo.id} />
                  </div>
              </div>
              <div className={"button-area mt-25"}>
                  <button className={"mid-btn sel-btn"} onClick={()=>setInquiryId(null)}>닫기</button>
                  <button className={"mid-btn upd-btn"}
                          onClick={()=>regAnswer({
                              'id': loadedAnsweredData.id,
                              'userId': loadedData.loginInfo.id,
                              'inquiryId' : loadedData.id,
                              'title' :inputTextValue,
                              'content' : textAreaValue,
                              'isFinished' : 'Y'
                          },setStates)}
                          disabled={loadedAnsweredData==null||loadedAnsweredData.isFinished==='Y'||
                          loadedAnsweredData.user.id!=loadedData.loginInfo.id}>등록</button>
              </div>
          </div>
      </div>
    );
}

export default PopupBox