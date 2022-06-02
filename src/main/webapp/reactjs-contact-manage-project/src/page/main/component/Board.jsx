import Table from "./Table";
import {useEffect, useState} from "react";
import {Redirect, useNavigate} from "react-router-dom";
import Pagination from "./Pagination";
import {getData, postData} from "../../../service/Fetch";

/**
 * 다건 접수
 */
function doAcceptCheckedList(states,setStates){
    const {setDidAccept} = setStates;
    const {inquiriesData} = states;

    const newList = [...inquiriesData.outList];
    const loginInfo = inquiriesData.loginInfo;
    const acceptData = [...newList.filter(row=>row.checked)
        .map(row=>{
            return {
                userId : loginInfo.id,
                inquiryId : row.id,
                isFinished : 'N'
            };
        })];

    const callbackFunc = {
        callbackOk() {
            alert('정상 처리되었습니다.');
            setDidAccept(true);
        }
    }

    if(acceptData==null || acceptData.length===0) {
        alert('접수할 대상이 없습니다.');
        return false;
    }
    if(acceptData.filter(row=>{
        if(row.userId == null || row.inquiryId == null || row.isFinished !=='N'){
            console.error('userId',row.userId);
            console.error('inquiryId',row.inquiryId);
            console.error('isFinished',row.isFinished);
            return true;
        }
    }).length >0) {
        alert('접수 대상 정보가 정상적으로 설정되지 않았습니다.');
        return false;
    }


    postData('/api/answers', acceptData, callbackFunc);
}

/**
 * 문의목록 다시 불러오기
 */
function refreshData({setInquiriesData, setErrorHttpStatus}){
    const URL='/api/inquiries';
    const callbackFunc = {
        callbackOk(data){
            setInquiriesData({...data});
        },
        setErrorHttpStatus : setErrorHttpStatus};
    let params = {
        size : 10,
        page : 1-1
    };

    getData(URL, params,callbackFunc);
}


function Board({setInquiryId}) {

    const [inquiriesData, setInquiriesData] = useState(null);
    const [isAutomatic, setAutomatic] = useState(true);
    const [errorHttpStatus, setErrorHttpStatus] = useState(0);
    const [didAccept, setDidAccept] = useState(false);
    const [count, setCount] = useState(10);
    const [timerId, setTimerId] = useState(0);
    const navigate = useNavigate();

    const states = {inquiriesData, isAutomatic, errorHttpStatus,didAccept};
    const setStates = {setInquiriesData, setAutomatic, setErrorHttpStatus, useNavigate, setDidAccept};

    useEffect(()=>{
        if(errorHttpStatus>0) {
            if (errorHttpStatus === 401)
                navigate('/');
            else {
                alert('처리 중 오류가 발생했습니다.');
            }
        }
    }, [errorHttpStatus]);

    // 접수 시 갱신
    useEffect(()=>{
        refreshData(setStates);
    },[didAccept]);

    //10초 Timer
    let timer;
    useEffect(()=>{
        let time = count;
        if(!isAutomatic) {
            console.log('!!!!!!!!!',timerId);
            return clearInterval(timerId);
        }
        else{
            timer = setInterval(()=> {
                if (time <= 0) {
                    time = 10;
                    setCount(time);
                    refreshData(setStates);
                } else {
                    setCount(--time);
                }
            },1000);
            setTimerId(timer);
        }
    },[isAutomatic])

    return (
        <>
            <div className={"top-filter-area-of-table text-align-right"}>
                <span style={{marginRight:'10px'}}>
                    <input id={"chkAutoSel"} type={"checkbox"} checked={isAutomatic} onChange={()=>setAutomatic(prevState => !prevState)}/>
                    <label style={{fontSize:'14px'}} htmlFor={"chkAutoSel"} >자동 갱신</label>
                    <span style={{display:"inline-block", width:'20px', fontSize:'13px'}}>({count})</span>
                </span>
                <button className={"sel-btn btn"} onClick={()=>{refreshData(setStates)}}>조회</button>
                <button className={"upd-btn btn"} onClick={()=>{doAcceptCheckedList(states,setStates)}}>접수</button>
            </div>
            <Table data={inquiriesData==null? null:[...inquiriesData.outList]} setInquiryId={setInquiryId} />
            <Pagination pageNo={1}></Pagination>
        </>
    )
}
export default Board;