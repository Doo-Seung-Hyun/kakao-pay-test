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
    const getInquiriesList=()=>{
        let inquiriesList = [
            {inquiryId:25, title: '안녕하세요', customerId: 'qqwer434', createdDateTime: '2022.05.23 18:23:55', userId: null,	userNm	:'', acceptedDateTime: null, finishedDateTime:null, isFinished: null	},
            {inquiryId:24, title: '문의드려요', customerId: 'doosh17', createdDateTime: '2022.05.22 17:23:55', userId: null, 	userNm	:'', acceptedDateTime: null, finishedDateTime:null, isFinished: null	},
            {inquiryId:23, title: '도대체 언제되나요', customerId: 'zhzhektm23', createdDateTime: '2022.05.19 18:23:56', userId: null,	userNm:'', acceptedDateTime: null, finishedDateTime:null, isFinished: null	},
            {inquiryId:22, title: '전화 통화가 너무힘들어요', customerId: 'sudent5353', createdDateTime: '2022.05.18 17:23:56', userId: 4,	userNm:'배수나', acceptedDateTime: '2022.05.23 18:23:55', finishedDateTime:null,		isFinished: 'N'},
            {inquiryId:21, title: '아 짜증나네요', customerId: 'kimkimko', createdDateTime: '2022.05.15 18:23:57', userId: 3,	userNm: '두승현', acceptedDateTime: '2022.05.22 17:23:55', finishedDateTime:null,		isFinished: 'N'},
            {inquiryId:20, title: '감사합니다', customerId: 'kakaosarang', createdDateTime: '2022.05.15 17:23:57', userId: 4,	userNm: '배수나', acceptedDateTime: '2022.05.19 18:23:56', finishedDateTime:null,		isFinished: 'N'},
            {inquiryId:19, title: '너무 고맙습니다', customerId: 'qwepo3', createdDateTime: '2022.05.14 18:23:58', userId: 1,	userNm: '김카카오', acceptedDateTime: '2022.05.18 17:23:56', finishedDateTime: '2022.05.23 18:23:55',	isFinished: 'N'},
            {inquiryId:18, title: '만족스럽네요', customerId: 'ccvd443dd', createdDateTime: '2022.05.14 17:23:58', userId: 2,	userNm:	'이페이', acceptedDateTime: '2022.05.15 18:23:57',	finishedDateTime: '2022.05.22 17:23:55',	isFinished: 'Y'},
            {inquiryId:17, title: '테스트입니다', customerId: 'asdf!sd%%', createdDateTime: '2022.05.13 18:23:59', userId: 3,	userNm: '두승현', acceptedDateTime: '2022.05.15 17:23:57', finishedDateTime:	'2022.05.19 18:23:56',	isFinished: 'Y'},
            {inquiryId:16, title: '상담사를 칭찬합니다', customerId: 'znznektm86', createdDateTime: '2022.05.13 17:23:59', userId: 4,	userNm: '배수나', acceptedDateTime: '2022.05.14 18:23:58', finishedDateTime: '2022.05.18 17:23:56', 	isFinished: 'Y'}
            // {inquiryId:15, title: '제 물건을 찾습니다', customerId: 'qojdioj%%', createdDateTime: '2022.05.13 14:23:22', userId: 1,	userNm: '김카카오', acceptedDateTime: '2022.05.14 17:23:58',	finishedDateTime: '2022.05.15 18:23:57',	isFinished: 'Y'},
            // {inquiryId:14, title: '출발안하나요?', customerId: 'pporrorro33', createdDateTime: '2022.05.12 17:23:13', userId: 2,	userNm	: '이페이', acceptedDateTime: '2022.05.13 18:23:59', finishedDateTime: '2022.05.15 17:23:57', isFinished: 'Y'},
            // {inquiryId:13, title: '안녕하세요', customerId: 'salesmantop', createdDateTime: '2022.05.12 12:55:44', userId: 1,	userNm: '김카카오', acceptedDateTime: '2022.05.13 17:23:59', finishedDateTime: '2022.05.14 18:23:58', isFinished: 'Y'},
            // {inquiryId:12, title: '문의드려요', customerId: 'topsecret1!', createdDateTime: '2022.05.12 10:23:14', userId: 3,	userNm: '두승현', acceptedDateTime: '2022.05.13 14:23:22	', finishedDateTime: '2022.05.14 17:23:58',	isFinished: 'Y'},
            // {inquiryId:11, title: '도대체 언제되나요', customerId: 'overtherainbow', createdDateTime: '2022.05.12 09:23:33', userId: 4,	userNm: '배수나', acceptedDateTime: '2022.05.12 17:23:13	', finishedDateTime: '2022.05.13 14:23:22',	isFinished: 'Y'}
        ];
        return inquiriesList;
    };

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
            <Pagination pageNo={13}></Pagination>
        </>
    )
}
export default Board;