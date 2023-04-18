function addParamsToUrlAndRefresh(value) {
    let url = window.location.href;
     if (url.indexOf('currentPage') === -1){
         if (url.indexOf('?') === -1){
             url += '?currentPage=' + value
         }
         else {
             url += '&currentPage=' + value
         }

    }
    else {
        let i = url.indexOf('currentPage') + 12;
        let k = 1;
        let oldValue;
        while(i + k <= url.length && url.charAt(i + k - 1) !== '&') {
            oldValue = url.substring(i, i + k)
            k++;
        }
        url = url.replace('currentPage=' + oldValue, 'currentPage=' + value)
    }
    window.location.href = url;
}

function setPageLinkFocused() {
    const urlParams = new URLSearchParams(window.location.search);
    if(urlParams.has("currentPage")) {
        let link = document.getElementById("btn-link" + urlParams.get("currentPage"));
        link.style.color = "#0056b3"
        link.style.textDecoration = "underline"
        console.log(link);
    }
    else{
        let link = document.getElementById("btn-link1")
        link.focus();
        link.style.color = "#0056b3"
        link.style.textDecoration = "underline"
        console.log(link);
    }

}