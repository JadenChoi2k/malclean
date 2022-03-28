const bodySlide = document.querySelector('#body-slide');
const slide = new bootstrap.Carousel(bodySlide, {
    interval: false,
    pause: false
});
bodySlide.addEventListener('slide.bs.carousel', () => window.scrollTo(0, 0));
bodySlide.addEventListener('slid.bs.carousel', updateBodyHeight);
updatePartinNumbers();
updateAreaNumber();
updateBodyHeight();

window.addEventListener('resize', updateBodyHeight);

function updateBodyHeight() {
    document.body.style.height = window.innerHeight + 'px';
    const contentHeight = document.querySelector('div.carousel-item.active').scrollHeight;
    if (contentHeight >= window.innerHeight)
        document.body.style.height = contentHeight + 'px';
}

function onRoleConfirmed() {
    slide.to(1);
}

function onMemberConfirmed() {
    slide.to(2);
}

// input으로 form을 생성해서 post로 전송한다.
// 1. 멤버 아이템들이 부들부들 떨다가 이리 저리 흩어진다.
// 2. 중앙에 말년청소라는 타이들이 뜨고 점점 확대되다가 post 제출을 한다.
function drawLottery() {
    const partinNum = getPartinNumber();
    const areaNum = getAreaNumber();

    if (partinNum != areaNum) {
        let result = confirm("수가 맞지 않습니다. 그래도 뽑습니까?");
        if (!result) {
            return;
        }
    }
    // alert('뽑으러 갑니다~');
    slide.to(1);
    partinMembersAnimate();
    setTimeout(() => sendPost(getParamsFromPage()), 3500);
}

function memberToggle(id) {
    const member = document.getElementById('member' + id);
    const partin = member.getAttribute('partin') === 'true' || member.getAttribute('partin') == true;
    const partinNumber = document.getElementById('partin-number');
    const image = member.getElementsByTagName('img')[0];
    if (partin) {
        member.setAttribute('partin', false);
        image.classList.replace('partin', 'not-partin');
    } else {
        member.setAttribute('partin', true);
        image.classList.replace('not-partin', 'partin');
    }
    updatePartinNumbers();
}

function updatePartinNumbers() {
    const numsElem = document.getElementsByClassName('partin-number');
    const partinNum = getPartinNumber();
    for (let i = 0; i < numsElem.length; i++) {
        numsElem[i].textContent = `현재원 : ${partinNum}명`;
    }
}

function partinMembersAnimate() {
    const members = document.getElementsByClassName('partin');
    for(let member of members) {
        member.style.animationName = 'shake';
        member.style.animationDuration = '0.3s';
        member.style.animationIterationCount = 'infinite';
        member.style.animationPlayState = 'running';
    }
    setTimeout(() => {
        for (let member of members) {
            member.style.animationName = 'go2pick';
            member.style.animationDelay = '1s';
            member.style.animationDuration = '1s';
            member.style.animationIterationCount = '1';
        }
    }, 1500);
    setTimeout(() => {
        for (let member of members) {
            member.style.animationPlayState = 'paused';
            member.style.width = '0';
        }
    }, 3450);
}

function getPartinNumber() {
    const members = document.getElementsByClassName('member');
    let count = 0;
    for (let i = 0; i < members.length; i++) {
        let member = members[i];
        if (member.getAttribute('partin') === 'true' || member.getAttribute('partin') == true)
            count++;
    }
    return count;
}

function getNextRole(roleId) {
    let curIndex = -1;
    for (let i = 0; i < roles.length; i++) {
        if (roles[i].id == roleId) {
            curIndex = i;
        }
    }
    if (curIndex === -1) {
        return;
    }
    return roles[(curIndex + 1) % roles.length];
}

// area : {id, name, minimumPeople}
function getAreaNumberComponent(area) {
    return `<div class="row text-center my-2 area">
                    <span class="col-md-3"></span>
                    <div class="col-6 col-md-3 h3"">${area.name}</div>
                    <div class="col-5 col-lg-3">
                        <div class="row m-0">
                            <button class="col-md-3 col-4 btn btn-outline-light"
                                    data-area="${area.id}" onclick="subArea('area$$${area.id}', '${area.minimumPeople}')"><h4>-</h4></button>
                            <label class="col-md-6 col-4 mt-2 h5 area-num" id="area$$${area.id}">${area.minimumPeople}</label>
                            <button class="col-md-3 col-4 btn btn-outline-light"
                                    data-area="${area.id}" onclick="addArea('area$$${area.id}')"><h5>+</h5></button>
                        </div>
                    </div>
                    <span class="col-1 col-md-3"></span>
                </div>`
}

// role: {id, areas, ...}
function changeRole(role) {
    currentRole = role;
    const lotteryNameInput = document.getElementById('lottery-name-input');
    lotteryNameInput.value = `${new Date().toLocaleDateString()} ${role.name} 뽑기.`;
    const prevAreas = document.getElementsByClassName("area");
    while (prevAreas.length > 0) {
        prevAreas[0].remove()
    }
    const newAreas = role.areas;
    const area_section = document.getElementsByClassName("area-section")[0];
    for (let newArea of newAreas) {
        const component = getAreaNumberComponent(newArea);
        const template = document.createElement('template');
        template.innerHTML = component;
        area_section.appendChild(template.content.firstChild);
    }
    updateAreaNumber();
}

function changeRoleById(roleId) {
    let nextRole = null;
    for (let role of roles) {
        if (role.id === roleId) {
            nextRole = role;
            break;
        }
    }
    if (nextRole == null) {
        return;
    }
    changeRole(nextRole);
}

function addArea(area) {
    const areaLabel = document.getElementById(area);
    const value = parseInt(areaLabel.textContent);
    const next = value + 1;
    if (next > 50) return;
    areaLabel.textContent = next.toString();
    updateAreaNumber();
}

function subArea(area, minSize) {
    const areaLabel = document.getElementById(area);
    const value = parseInt(areaLabel.textContent);
    const next = value - 1;
    if (next < minSize) {
        alert(`이 구역은 필수적으로 ${minSize}명 이상 청소해야 합니다.`);
        return;
    }
    areaLabel.textContent = next.toString();
    updateAreaNumber();
}

function updateAreaNumber() {
    const number = getAreaNumber();
    const pickNumbers = document.getElementsByClassName('pick-number');

    for (let i = 0; i < pickNumbers.length; i++) {
        pickNumbers[i].textContent = `현재 구역 수 : ${number}`;
    }
}

function getAreaNumber() {
    const mainAreas = document.getElementsByClassName('area-num');
    let count = 0;
    for (let i = 0; i < mainAreas.length; i++) {
        count += parseInt(mainAreas[i].textContent);
    }
    return count;
}

// params : JSON (name, value)
function sendPost(params) {
    let form = document.createElement('form');
    form.setAttribute('method', 'post');
    form.setAttribute('action', '');
    document.charset = 'utf-8';
    for (let key in params) {
        let hiddenField = document.createElement('input');
        hiddenField.setAttribute('type', 'hidden');
        hiddenField.setAttribute('name', key);
        hiddenField.setAttribute('value', params[key]);
        form.appendChild(hiddenField);
    }
    document.body.appendChild(form);
    form.submit();
}

function getParamsFromPage() {
    const lotteryName = document.getElementById('lottery-name-input').value;
    const members = document.getElementsByClassName('member');
    const areas = document.getElementsByClassName('area-num');
    const currentRoleID = currentRole.id;
    let memberIds = [];
    let areaIds = [];

    for (let i = 0; i < members.length; i++) {
        const partin = members[i].getAttribute('partin') === 'true'
                        || members[i].getAttribute('partin') == true;
        if (!partin)
            continue;
        let memberId = members[i].getAttribute('id').split('member')[1];
        memberId = parseInt(memberId);
        memberIds.push(memberId)
    }

    for (let i = 0; i < areas.length; i++) {
        const mainArea = areas[i];
        const count = parseInt(mainArea.textContent);
        const area = mainArea.getAttribute('id').split('area$$')[1];
        for (let j = 0; j < count; j++) {
            areaIds.push(area);
        }
    }

    return {
        lotteryName: lotteryName,
        participants: memberIds.join(','),
        roleId: currentRoleID,
        pick: areaIds.join(','),
    }
}