import {useState} from 'react';
import {useNavigate} from "react-router-dom";

function doLogin(event, userEmailAddr, password, navigator){
    event.preventDefault();

    const LOGIN_URL = "/users/signin";
    const dataObj = {userEmailAddr,password};
    const regexp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

    if(userEmailAddr==null){
        alert('이메일 주소를 입력해주세요');
        return;
    }else if(userEmailAddr.match(regexp)==null){
        alert('잘못된 메일주소 형식입니다.');
        return;
    }else if(password==null || password.length===0){
        alert('비밀번호를 입력해주세요');
        return;
    }

    fetch(LOGIN_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        },
        body: new URLSearchParams(dataObj)
    })
        .then((response) => response.json())
        .then((data) => {
            if(data.error!=null) {
                alert(data.error);
                return;
            }
            navigator('/main');
        })
        .catch((error) => {
            console.error('실패:', error);
        });
}

function SignIn() {

    const [userEmailAddr, setUserEmailAddr] = useState(null);
    const [password, setPassword] = useState(null);
    const navigator = useNavigate();

    const getInquiryContent = (e) =>{
        e.preventDefault();
        const LOGIN_URL = "/api/inquiries/2";

        fetch(LOGIN_URL, {
                method: 'GET'
        })
            .then((res)=>res.json())
            .then((data)=>{
                console.log('성공',data);
            })
            .catch((error)=>{
                console.log('오류',error);
            });
    }

    return (
        <div className="main-background">
            <form className={"login-box container"}>
                <div className={"title mt-15"}>
                    <div className={"logo"}>Contact-Center Manager</div>
                    <div className={"mt-20"}>로그인</div>
                </div>
                <label className={"mt-25"}>
                    <span>이메일</span>
                    <input className={"mt-5"} type={"text"} id={"userEmailAddr"} name="userEmailAddr" placeholder="이메일주소를 입력해주세요"
                           onChange={e=>setUserEmailAddr(e.target.value)}/>
                </label>
                <label className={"mt-25"}>
                    <span>비밀번호</span>
                    <input className={"mt-5"} type={"password"} name={"password"} placeholder="비밀번호를 입력해주세요"
                           onChange={e=>setPassword(e.target.value)}/>
                </label>
                <button className={"mt-40 upd-btn"} type="submit" onClick={e=>doLogin(e, userEmailAddr, password, navigator)}>로그인</button>
            </form>
        </div>
    );
}

export default SignIn;