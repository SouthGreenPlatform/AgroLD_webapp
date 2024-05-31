const LocalStorageDataConsent = {
    "sparqlEditor.history.enabled": "Store your history of sparql requests",
    "advancedSearch.history.enabled": "Store your history of advanced searches"
}

const defaultLocalStorageData = Object.keys(LocalStorageDataConsent)

const isLocalStorageAvailable = () => typeof localStorage !== 'undefined'

const acceptLocalStorage = (accepted) => {
    sessionStorage.setItem("acceptLocalStorage", accepted)
    document.getElementById("localStorage-ask")?.classList.replace("slide-in", "slide-out"); 
    console.log(accepted? "accepted": "refused");
}

const hasAcceptedLocalStorage = () => sessionStorage.getItem("acceptLocalStorage") === "true"

const consentedToTreatment = (key) => {
    if (hasAcceptedLocalStorage()) {
        switch (key) {
            //case "advancedSearch.xxx":
            case "advancedSearch.history":
                return sessionStorage.getItem("advancedSearch.history.enabled") === "true"

            //case "sparqlEditor.xxx":
            case "sparqlEditor.history":
                return sessionStorage.getItem("sparqlEditor.history.enabled") === "true"

            default:
                return false
        }
    }
    return false
}

const localStorageSet = (key, val) => isLocalStorageAvailable() && 
    consentedToTreatment(key) && 
    localStorage.setItem(key, val)

const localStorageGet = (key) => {
    if (isLocalStorageAvailable() && consentedToTreatment(key)) {
        return localStorage.getItem(key)
    } 
    return null
}


const acceptData = (arrayOfAuthorizedData) => {
    if(isLocalStorageAvailable()){
        acceptLocalStorage(true)
        arrayOfAuthorizedData.forEach(data => {
            sessionStorage.setItem(data, "true")
        })
    }
}

const acceptAllData = () => {
    acceptData(defaultLocalStorageData)
}

