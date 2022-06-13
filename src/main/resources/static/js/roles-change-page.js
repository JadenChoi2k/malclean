// 테이블에 있는 아이디 순서대로 hidden input에 삽입해준다.
function updateInputFields() {
    const roleIdsInput = document.getElementById('roleIds');
    const roleIdsColumn = document.getElementsByClassName('role-id-col');
    const roleIdList = [];
    for (let i = 0; i < roleIdsColumn.length; i++) {
        roleIdList.push(roleIdsColumn[i].innerText);
    }
    roleIdsInput.value = roleIdList.join(',');
}

function updateSequence() {
    const rolesTable = document.getElementById('role-change-table');
    const roleSequenceColumn = rolesTable.getElementsByClassName('role-sequence-col');
    roleSequenceColumn[0].innerHTML = '✅';
    for (let i = 1; i < roleSequenceColumn.length; i++) {
        if (roleSequenceColumn[i].innerHTML == '✅') {
            roleSequenceColumn[i].innerHTML = '';
            return true;
        }
    }
}

function fetchRoleInfo(row) {
    let roleId = row.getElementsByClassName('role-id-col')[0].innerText * 1;
    getRoleFromServer(roleId, (data)=>alert(JSON.stringify(data.data)));
}

// 테이블의 행 클릭 시 팝업 형태로 보여준다.
function getRoleFromServer(roleId, cbk) {
    fetch(location.origin + `/api/v1/role/${roleId}`,
        {method:'GET'})
        .then(resp => resp.json())
        .then(cbk)
}