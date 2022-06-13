const domParser = new DOMParser();
const AREA_API_PATH = '/api/v1/area';

function getAreaInput() {
    return `<div class="input-group main-area-input mb-2">
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="구역 이름" name="areaNames">
                    
                    <span class="input-group-text">
                        <button type="button" class="btn-close" aria-label="Close"></button>
                    </span>
                </div>
                <div class="input-group border-top-0 my-2">
                    <select class="form-select form-select-sm" name="difficulties">
                        <option selected>난이도</option>
                        <option value="0">0</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="6">6</option>
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9</option>
                        <option value="10">10</option>
                    </select>
                    <select class="form-select form-select-sm" name="minimumPeoples">
                        <option selected>최소인원</option>
                        <option value="0">0</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="6">6</option>
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9</option>
                        <option value="10">10</option>
                    </select>
                    <select class="form-select form-select-sm" name="changeable">
                        <option selected>인수인계</option>
                        <option value="true">한다</option>
                        <option value="">하지 않는다.</option>
                    </select>
                </div>
            </div>`;
}

function onRemoveClicked(event) {
    alert(event);
}

function addMainAreaInput() {
    const inputGroup = document.getElementById("main-area");
    const addButton = inputGroup.getElementsByClassName("add-section")[0]
    const inputText = getAreaInput();
    const textInput = domParser.parseFromString(inputText, 'text/html').
                        getElementsByClassName("main-area-input")[0];
    textInput.getElementsByTagName("button")[0].addEventListener('click',
            event => {
                event.path[2].remove();
                event.path[3].remove();
            });
    inputGroup.insertBefore(textInput, addButton);
}

function getToRoleEdit(roleId) {
    location.href = `/team/roles/${roleId}/edit`;
}

function confirmPost(message, action) {
    let result = confirm(message);
    if (result) postRequest(action);
}

// 파라미터를 넘기지 않는다.
function postRequest(action) {
    let form = document.createElement("form");
    form.setAttribute('method', 'post');
    form.setAttribute('action', action);
    document.charset = 'utf-8';
    document.body.appendChild(form);
    form.submit();
}

/**
 * role edit page에서 쓰이는 함수들.
  */

function getRoleId() {
    return parseInt(document.getElementById('id').value);
}

function getAreaFromServer(areaId) {
    return fetch(`${AREA_API_PATH}/${areaId}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            if (data.result === 'SUCCESS') {
                return data.data;
            } else {
                return null;
            }
        });
}

// area: {areaName, difficulty, minimumPeople, changeable}
function createArea(newArea) {
    newArea.roleId = getRoleId();
    fetch(`${AREA_API_PATH}/`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json; charset=utf-8'},
        body: JSON.stringify(newArea),
    })
        .then(response => response.json())
        .then(data => {
            if (data.result === 'SUCCESS') {
                alert('추가되었습니다.');
                location.reload();
            } else {
                alert('추가에 실패하였습니다.');
            }
        });
}

// area: {areaId, name, difficulty, minimumPeople, changeable}
function editArea(changedArea) {
    console.log(`editArea(${JSON.stringify(changedArea)})`);
    fetch(`${AREA_API_PATH}/${changedArea.areaId}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json; charset=utf-8'},
        body: JSON.stringify(changedArea),
    })
        .then(response => response.json())
        .then(data => {
            if (data.result === 'SUCCESS') {
                alert('수정되었습니다.')
                location.reload();
            } else {
                alert('수정에 실패하였습니다.');
            }
        });
}

function deleteArea(areaId) {
    fetch(`${AREA_API_PATH}/${areaId}`, {
        method: 'DELETE',
    })
        .then(response => response.json())
        .then(data => {
            if (data.msg === 'success') {
                alert('삭제되었습니다.');
                const area = document.getElementById(`area$$${areaId}`);
                if (area != null) {
                    area.remove();
                }
            } else {
                if (data.code === 401) {
                    alert('삭제 권한이 없습니다.');
                } else if (data.code === 404) {
                    alert('구역이 존재하지 않습니다.');
                } else {
                    alert('삭제에 실패했습니다.');
                }
            }
        });
}