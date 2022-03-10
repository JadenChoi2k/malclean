function askConfirm(msg, url) {
    let result = confirm(msg);
    if (result) {
        location.href = url;
    }
}