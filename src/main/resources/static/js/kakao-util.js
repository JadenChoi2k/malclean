// init const
const JAVASCRIPT_KAKAO_ID = '0fa2a3587e1ed01fb74516328fbec987';
const APP_DOMAIN = "http://mil.malclean.kr";
// const APP_DOMAIN = "http://localhost:8080";
// initialize kakao api here
initKakao();
const kakao_access_token = getCookie('authorize-access-token');
window.Kakao.Auth.setAccessToken(kakao_access_token);
//

function initKakao() {
    window.Kakao.init(JAVASCRIPT_KAKAO_ID);
}

function kakaoLogin() {
    const url = new URL(location.href);
    let redirect = url.searchParams.get('redirectURI');
    if (redirect) {
        setCookie('redirectURI', redirect, 5)
    }
    window.Kakao.Auth.authorize({
        redirectUri: APP_DOMAIN + "/member/join-pass"
    })
}

function kakaoLogout() {
    if (!window.Kakao.Auth.getAccessToken()) {
        console.log('Not logged in.');
    } else {
        window.Kakao.Auth.logout(function() {
            console.log(Kakao.Auth.getAccessToken());
            alert('로그아웃되었습니다.');
            window.location.href = APP_DOMAIN + '/member/logout';
        });
    }
}

function unlinkApp() {
    Kakao.API.request({
        url: '/v1/user/unlink',
        success: function (res) {
            alert('success: ' + JSON.stringify(res))
            window.location.href = APP_DOMAIN + '/member/delete';
        },
        fail: function (err) {
            alert('fail: ' + JSON.stringify(err))
        },
    })
}

function getOutOfTeam() {
    location.href = APP_DOMAIN + '/member/team-out';
}

function getMe(success) {
    Kakao.API.request({
        url: '/v2/user/me',
        success: success,
        fail: function(error) {
            alert(
                'failed to request user information: ' +
                JSON.stringify(error)
            )
        },
    })
}

function isAuthorizedToInvite() {
    return Kakao.API.request({
        url: '/v2/user/scopes',
        data: {scopes:['friends', 'talk_message']},
    });
}

function createInviteAndShowPopup() {
    fetch(APP_DOMAIN + '/invite/create', {
        method: 'GET',
    }).then(response => {
        return response.json()
    }).then(function(data) {
        window.Kakao.Link.sendCustom({
            templateId: 69677,
            templateArgs: data
        })
    });
}

function sendInvite() {
    isAuthorizedToInvite()
        .then(data => {
            return data.scopes.length === 2;
        }).then(isAuth => {
            if (isAuth) createInviteAndShowPopup();
            else window.Kakao.Auth.login({
                scope: 'friends, talk_message'
            }).then(() => createInviteAndShowPopup());
        })
}

function setCookie(name, value, exp) {
    let date = new Date();
    date.setTime(date.getTime() + exp * 60 * 1000);
    document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
}

function getCookie(name) {
    const value = "; " + document.cookie;
    const parts = value.split("; " + name + "=");
    if (parts.length === 2) return parts.pop().split(";").shift();
}