import {useEffect} from "react";

function Pagination({pageNo}) {
    const PAGES = 5;
    let pageNoList =null;
    const getPageNoList=()=>{
        let arr=[];
        let firstPageNo = Math.floor((pageNo - 1) / PAGES)*PAGES+1;
        for (let i = firstPageNo; i<firstPageNo+PAGES; i++) {
            arr.push(i);
        }
        return arr;
    }
    return (
        <div className={"pagination mt-15"}>
            <a href={"#"}>&laquo;</a>
            {getPageNoList().map(pageno=>
                <a href={"#"} className={pageno===pageNo?"active":""}>{pageno}</a>
            )}
            <a href={"#"}>&raquo;</a>
        </div>
    )
}
export default Pagination;