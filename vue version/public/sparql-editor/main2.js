
$(document).ready(function () {

    document.getElementById("fileToLoad").onchange = function () {
        document.getElementById("uploadFile").value = this.value;
    };
    // Display the list of patterns            
    var divpatt = document.getElementById("patternslist");
    var ol = document.createElement("ol");
    divpatt.innerHTML += "<ol>";
    for (i = 0; i < patternlabels.length; i++) {
        var li = document.createElement("li");
        li.innerHTML = patternlabels[i] + " (<a href=\"#\" onclick=\"selectPattern(" + i + ")\">select</a>)";
        ol.appendChild(li);
        //divpatt.innerHTML += //qpatterns[selectedpattern][i] + ': <input class="aparameter" value="' + qpatterns[selectedpattern][i] + '" oninput="replaceParaValue(' + "/" + qpatterns[selectedpattern][i] + "/g" + ', this)"/><br><br>';
    }
    divpatt.appendChild(ol);

    const historyStorage = localStorageGet("sparqlEditor.history");
    if (historyStorage) {
        const history = JSON.parse(historyStorage);
        const historyContent = document.getElementById("history")
        const btnHistory = document.getElementById("btnHistory");

        btnHistory.setAttribute("data-step", "9");
        btnHistory.setAttribute("data-intro", "You can insert previously ran queries");

        document.getElementById("historyRow")?.classList
            .replace("d-none", "d-block");

        history.forEach((historyElt, index) => {
            const div = document.createElement("div");
            const code = document.createElement("pre");
            const selectBtn = document.createElement("button");

            code.innerText = historyElt;
            code.setAttribute("style", "background-color: #d9d9d9;");
            code.classList.add("p-2")

            selectBtn.classList.add("btn", "btn-info")
            selectBtn.setAttribute("style", "cursor: pointer;");
            selectBtn.setAttribute("type", "button");
            selectBtn.innerText = "Select";
            selectBtn.onclick = () => {
                yasqe.setValue(historyElt);
                $("#historyModal").modal('hide')
            }

            div.appendChild(code);
            div.appendChild(selectBtn);
            historyContent.appendChild(div);
            if (index < history.length - 1)
                historyContent.appendChild(document.createElement("hr"));
        })
    }
});