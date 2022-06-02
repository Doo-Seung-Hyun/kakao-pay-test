export function putData(url,data,callbackFunc,props){
    postData(url, data, callbackFunc, props, 'PUT');
}

export function postData(url, data, callbackFunc, props, method) {
    method = method||'POST';
    if(props==null){
        props = {
            mode: 'cors',
            cache: 'no-cache',
            credentials: 'same-origin',
            headers: {
                'Content-Type': 'application/json',
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
            redirect: 'follow',
            referrerPolicy: 'no-referrer',
        }
    }
    props.method = method;
    if(props.headers["Content-Type"] === 'application/json')
        props.body = JSON.stringify(data);

    fetchData(url, props, callbackFunc);
}
export function getData(url, param, callbackFunc, props) {
    if(props==null){
        props = {
            mode: 'cors',
            cache: 'no-cache',
            credentials: 'same-origin',
            redirect: 'follow',
            referrerPolicy: 'no-referrer',
        }
    }
    props.method ='GET';
    if(param!=null){
        let paramString = new URLSearchParams(param).toString();
        url = url+'?'+paramString;
    }

    fetchData(url, props, callbackFunc);
}

function fetchData(url,props, callbackFunc) {
    fetch(url, props)
    .then(res => {
        if (res.status !== 200 && res.status !== 201) {
            if(callbackFunc.setErrorHttpStatus!=null) {
                callbackFunc.setErrorHttpStatus(res.status);
            }
            throw new Error();
        }
        return res.json();
    })
    .then(data => {
        if(callbackFunc.callbackOk !=null) callbackFunc.callbackOk(data);
        console.log(data);
    })
    .catch((error) => {
        if(callbackFunc.callBackFail!=null) callbackFunc.callBackFail(error);
        console.log('오류', error);
    });
}