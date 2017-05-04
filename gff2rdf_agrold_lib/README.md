gff2rdf Lib - Quickly and easy function for annote and transflate GFF file in RDF
========================================================

This module translate all gff file in RDF, it connect with agroportale for annote the rdf output,
For this version the ttl output is possible. 

1- Installation

    pip install gff2rdf_lib 


2 - Prerequisites

Running 

    - python 2.7
    - librairies: re, urllib2, urllib2, json, uuid, fileinput, time, namedtuple, gzip, urllib
      

3 - Use

    >>> from gff2rdf_lib import agrold
    
    >>> gff_model2rdf(path_input_file, path_output_file, text_to_annotate, adress_uri, alias_chromosome_file=1)

    *path_input_file: the gff file to translate
    *path_output_file: The path where the file should be saved
    *text_to_annotate: the name of the specie e.a: Oryza sativa Indica Group
    *adress_uri: the uri adresse for take prefix at datas e.a: http://www.southgreen.fr/agrold/whead_graph/
    *alias_chromosome_file: some time into file in gff a alias are use for def the chromosome, but this param is optionnel
     
    e.a:
     
    exempleGffFile.gff:
     
    traes3bPseudomoleculeV1	GDEC	region	1	774434471	.	.	.	ID=traes3bPseudomoleculeV1;Name=traes3bPseudomoleculeV1
    traes3bPseudomoleculeV1	GDEC	scaffold	348386688	349797793	.	+	.	ID=v443_0001;Name=v443_0001;annotation_type=IWGSC;databank_used=PfamA(v26),uniprot(v2012_04),protOsMSU(),embl_est_pln(v111),uniprot_sprot(v2012_04);software_used=Blast(2.2.21),HMMscan(3.0);OldId=v443_0001
    traes3bPseudomoleculeV1	GDEC	scaffold	518597965	519187914	.	+	.	ID=v443_0002;Name=v443_0002;annotation_type=IWGSC;databank_used=PfamA(v26),uniprot(v2012_04),protOsMSU(),embl_est_pln(v111),uniprot_sprot(v2012_04);software_used=Blast(2.2.21),HMMscan(3.0);OldId=v443_0002
    traes3bPseudomoleculeV1	GDEC	rRNA	327899570	327899695	.	-	.	ID=TRAES3BF133500010CFD_rrna;Note=5S_rRNA.1;OldId=rRNA82

    
    >>> we execute gff_model2rdf('/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/urgi/pseudomolecul_wheat.gff', '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/urgi_ttl/pseudomolecul_wheat.ttl', "Oryza sativa Indica Group", "http://www.southgreen.fr/agrold/whead_graph", '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/urgi/alias_file')
        
    @base	<http://www.southgreen.fr/agrold/whead_graph/> .
    @prefix	rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
    @prefix	rdfs:<http://www.w3.org/2000/01/rdf-schema#> .
    @prefix	xsd:<http://www.w3.org/2001/XMLSchema#> .
    @prefix	owl:<http://www.w3.org/2002/07/owl#> .
    @prefix	agrold_vocabulary:<http://www.southgreen.fr/agrold/vocabulary/> .
    @prefix	obo:<http://purl.obolibrary.org/obo/> .
    @prefix	chromosome:<http://www.southgreen.fr/agrold/chromosome/> .
    @prefix	agrold_schema:<http://www.southgreen.fr/agrold/resource/> .
    @prefix	region:<http://www.southgreen.fr/agrold/whead_graph/region/> .
    @prefix	scaffold:<http://www.southgreen.fr/agrold/whead_graph/scaffold/> .
    @prefix	rRNA:<http://www.southgreen.fr/agrold/whead_graph/rRNA/> .
    @prefix	ncRNA:<http://www.southgreen.fr/agrold/whead_graph/ncRNA/> .
    @prefix	tRNA:<http://www.southgreen.fr/agrold/whead_graph/tRNA/> .
    @prefix	BAC:<http://www.southgreen.fr/agrold/whead_graph/BAC/> .
    @prefix	mRNA:<http://www.southgreen.fr/agrold/whead_graph/mRNA/> .
    @prefix	CDS:<http://www.southgreen.fr/agrold/whead_graph/CDS/> .
    @prefix	gene:<http://www.southgreen.fr/agrold/whead_graph/gene/> .
        
    >>> gffparser(path_file)
     
    can be to use for just parser a gff fil, it create a dictionary.
     
    The GFF3 Parser is a generic fonction who build a dictionary, it easy to browse this dictionary for build RDF data
     
    e.a: 
     
     {   'attributes': {   'Dbxref': 'InterPro:IPR005333,InterPro:IPR017887',
                          'ID': 'BGIOSGA000770-TA',
                          'Name': 'BGIOSGA000770-TA',
                          'Parent': 'BGIOSGA000770'},
        'end': 35414873,
        'phase': None,
        'score': None,
        'seqid': 'Osi01',
        'source': 'glean',
        'start': 35413950,
        'strand': '-',
        'type': 'mRNA'},
    {   'attributes': {   'Derives_from': 'BGIOSGA000770-TA',
                          'ID': 'BGIOSGA000770-TA_protein',
                          'Name': 'BGIOSGA000770-TA'},
        'end': 35414873,
        'phase': None,
        'score': None,
        'seqid': 'Osi01',
        'source': 'glean',
        'start': 35413950,
        'strand': '-',
        'type': 'polypeptide'},
    {   'attributes': {   'Parent': 'BGIOSGA000770-TA'},
        'end': 35414873,
        'phase': '0',
        'score': None,
        'seqid': 'Osi01',
        'source': 'glean',
        'start': 35413950,
        'strand': '-',
        'type': 'CDS'},
    {   'attributes': {   'Parent': 'BGIOSGA000770-TA'},
        'end': 35414873,
        'phase': None,
        'score': None,
        'seqid': 'Osi01',
        'source': 'glean',
        'start': 35413950,
        'strand': '-',
        'type': 'exon'},
     
     
alias_filee.txt

alias_file.txt: some time into file in gff a alias are use for def the chromosome, but this param is optionnel.
if you usean alias for you chromosome we recommande to complete the file:

e.a: make the prefix in left and the chromosome in right 
alias_file.txt
traes3bPseudomoleculeV1=3B
   


licence GPL. 