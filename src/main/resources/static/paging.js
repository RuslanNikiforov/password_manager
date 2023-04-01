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