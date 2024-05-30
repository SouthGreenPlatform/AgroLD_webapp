// https://stackoverflow.com/questions/16427636/check-if-localstorage-is-available
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

const acceptFromSelection = (e) => {
    e.preventDefault()

    acceptData(
        Object.entries(
            Object.fromEntries(new FormData(e.target)) // retireve the form data 
        ).filter(([_, value]) => value === "on").map(([key, _]) => key) // only keep ticked checkboxes
    )

    // ugly but hey that works! 
    // we cannot acutally close the modal from the form in bootstrap
    document.getElementById("close-storage-modal")?.click()
    return false
}

const popup = document.getElementById("localStorage-ask")
document.getElementById("labelstore-form")?.addEventListener("submit", acceptFromSelection)

console.log("localStorage available: " + isLocalStorageAvailable())
 
if(isLocalStorageAvailable() && sessionStorage.getItem("acceptLocalStorage") === null){
    if (popup) popup.style.visibility = "visible";


    const consentList = document.getElementById('consent-inputs')

    Object.entries(LocalStorageDataConsent).forEach(([key, value]) => {
        consentList.appendChild(
            document.createElement("hr")
        )
        const input = document.createElement("input");
        Object.entries({
            class: "form-check-input",
            type: "checkbox",
            role: "switch",
            style: "margin: 5px;",
            id: key,
            name: key,
            checked: "true",
        }).forEach(([key, value]) => input.setAttribute(key, value))
        
        consentList.appendChild(input)

        const label = document.createElement("label")
        Object.entries({
            class: "form-check-label",
            for: key,
        }).forEach(([key, value]) => input.setAttribute(key, value))

        label.textContent = value
        consentList.appendChild(label)
    })
} else {
    if (popup) popup.style.display = "none"; 
}