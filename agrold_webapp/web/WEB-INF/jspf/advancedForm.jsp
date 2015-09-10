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
        <select class="yasrbtn arrow" id="type" name="type">
            <!--option value="--">--- Select an element ---</option-->
            <option value="gene" selected>Gene</option>
            <option value="protein">Protein</option>
            <option value="qtl">QTL</option>
            <option value="pathway">Pathway</option>
            <option value="ontology">Ontology</option>
        </select>
        <input id="input" class="keyword" name="keyword" type="text" autofocus style="display: inline"/>
        <input id="submit" class="yasrbtn" type="submit" value="Search" style="display: inline"/>
    </form>
</center>