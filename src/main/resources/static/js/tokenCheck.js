const token = localStorage.getItem('token');
    if(token){
        const payload = JSON.parse(atob(token.split('.')[1]));
        const exp = payload.exp;
        const now = Math.floor(Date.now() / 1000);

        console.log("exp: " + exp);
        console.log("now: " + now);

        if(exp < now){
            if(confirm("사용자 인증이 만료되었습니다. 연장하시겠습니까?")){
                const xhr = new XMLHttpRequest();
                xhr.onload = function(){
                    const newToken = xhr.responseText;

                    localStorage.setItem('token', newToken);
                }
                xhr.open('POST', '/token/extend');
                xhr.setRequestHeader('Authorization', token);
                xhr.send();

            } else{
                localStorage.removeItem('token');
                location.href="/login";
            }
        }
    } else{
        alert("사용자 인증 정보가 존재하지 않습니다.");
        location.href="/login";
    }