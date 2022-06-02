function Table({data,chkBoxClickHandler,setInquiryId}) {
    let rows;
    if(data!=null) {
        rows= data.map(row=>
                <tr>
                    <td><label className={"fullwidth"}>
                        <input type={"checkbox"} onClick={event=>row.checked=true}></input>
                    </label></td>
                    <td>{row.id}</td>
                    <td className={"text-overflow"} onClick={()=>setInquiryId(row.id)}>{row.title}</td>
                    <td className={"text-overflow"}>{row.customerId}</td>
                    <td className={"dv-col"} style={{lineHeight:'1.3'}}>{row.createdDateTime}</td>
                    <td>{row.answered? '완료': row.acceptedDateTime!=null? '접수':'미접수'}</td>
                    <td style={{lineHeight:'1.3'}}>{row.acceptedDateTime}</td>
                    <td style={{lineHeight:'1.3'}}>{row.finishedDateTime }</td>
                </tr>
        );
    }
    return (
        <div className={"table-wrapper"}>
            <table className={"inquiriesTable"}>
                <thead>
                    <tr>
                        <th></th>
                        <th>번호</th>
                        <th>제목</th>
                        <th>고객ID</th>
                        <th className={"dv-col"}>작성일시</th>
                        <th>상태</th>
                        <th>접수일시</th>
                        <th>답변일시</th>
                    </tr>
                </thead>
                <tbody>
                    {rows}
                </tbody>
            </table>
        </div>
    );
}

export default Table;