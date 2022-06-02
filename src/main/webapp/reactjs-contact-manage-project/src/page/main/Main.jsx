import {useState} from 'react';
import Title from './component/Title';
import Board from './component/Board';
import Container from "./component/Container";
import PopupBox from "./component/PopupBox";

function Main() {
    const containerStyles ={
        minWidth : '1080px',
        height : '85%',
        minHeight: '860px',
        padding : '40px',
        overflow : 'auto'
    }
    const [inquiryId, setInquiryId] = useState(null);
    return (
        <>
          <div className={"main-background"}>
              <Container styles={containerStyles}>
                  <Title>질문 목록</Title>
                  <div>고객의 질문을 모니터링하고, 답변을 작성할 수 있습니다.</div>
                  <Board setInquiryId={setInquiryId}></Board>
              </Container>
          </div>
            <>{inquiryId!=null? <PopupBox inquiryId={inquiryId} setInquiryId={setInquiryId}/>:null}</>
        </>
    );
}

export default Main;