<%-- 
    Document   : advancedForm
    Created on : Sep 8, 2015, 2:21:47 PM
    Author     : tagny
--%>
<div style="text-align: center">
    <p>Search examples: ontological concepts - 'plant height' or 'regulation of gene expression'; gene names -
        'GRP2' or 'TCP2'.</p>
    <p>QTL ID: 'AQAA003' ; protein name: 'TBP1'</p>
</div>
<center>
    <form id="sform">
        <span style="color:#ff0000" id="message"></span><select class="yasrbtn arrow" id="type" name="type">
            <option value="" default>--- Select a type* ---</option>
            <option value="gene">Gene</option>
            <option value="protein">Protein</option>
            <option value="qtl">QTL</option>
            <option value="pathway">Pathway</option>
            <option value="ontology">Ontology</option>
        </select>
        <input id="input" class="keyword" name="keyword" type="text" autofocus placeholder="Type here ..." style="display: inline"/>
        <input id="submit" class="yasrbtn" type="submit" value="Search" style="display: inline"/>
    </form>
</center>
<script>
    $('#sform').submit(function (e) {
        // custom handling here
        var selectElt = document.getElementById("type");
        //alert(selectElt)
        var selectedValue = selectElt.options[selectElt.selectedIndex].value;
        if (selectedValue === "") {
            $(selectElt).css("border", "2px solid #ff0000");
            $("#message").html('Select a type please');
            //alert()
            e.preventDefault();
        }
    });
</script>