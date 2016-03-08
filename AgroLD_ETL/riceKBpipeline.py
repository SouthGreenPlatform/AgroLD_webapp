#!/usr/bin/env python


import glob
#from Bio.UniProt import GOA
import pprint
#from riceKB import * #southGreenParsers #oryzaBaseParser gafToRDF grameneParsers TairParser grameneParsers
#import riceKB
import os
import re
from riceKB import gafToRDF, grameneParsers, oryzaBaseParser, TairParser,\
    southGreenParsers, uniprotToRDF, TropgeneParser, TropgeneModel, gffParser, os_japonicaModel, \
    os_indicaModel, a_thalianaModel, sniplayPaserModel
#from riceKB.oryzaBaseParser import oryzaBaseParser



'''
 Input directory path
'''
'''
 Ontology association directory/files
'''
eco_map_file = '/home/venkatesan/workspace/explore/test_files/ontology_associations/gaf-eco-mapping.txt'
prot_assoc_test_dir = '/home/venkatesan/workspace/explore/test_files/ontology_associations/protein_associations/*.*'
gene_assoc_test_dir = '/home/venkatesan/workspace/explore/test_files/ontology_associations/gene_associations/*.*'
qtl_assoc_test_dir = '/home/venkatesan/workspace/explore/test_files/ontology_associations/qtl_associations/*.*'
#aracyc_file = '/home/venkatesan/workspace/explore/test_files/tair/aracyc/aracyc_pathways.20130709.txt'

protein_assoc_dir = '/media/sf_F_DRIVE/IBC/argoLD_project/data/ontology_associations/protein/*.*' # /home/venkatesan/workspace/argoLD_project/data/ontology_associations/protein
gene_assoc_dir = '/home/venkatesan/workspace/argoLD_project/data/ontology_associations/gene/*.*'
qtl_assoc_dir = '/media/sf_F_DRIVE/IBC/argoLD_project/data/ontology_associations/qtl/*.*'

'''
Gramene genes and QTL directory/files
'''
gramene_g_test_dir = '/home/venkatesan/workspace/explore/test_files/gramene_genes/*.txt' #*.txt Oryza_sativa_japonica.txt' #meridionalis barthii sativa_indica sativa_japonica
gramene_qtl_test_file = '/home/venkatesan/workspace/explore/test_files/gramene_qtl/Rice_QTL.dat'

gramene_genes_files = '/media/sf_F_DRIVE/IBC/argoLD_project/data/gramene_genes/*.txt' 
gramene_qtl_file = '/media/sf_F_DRIVE/IBC/argoLD_project/data/gramene_qtl/Rice_QTL.dat'
'''
OryzaBaseDB
''' 
oryzabase_test_file = '/home/venkatesan/workspace/explore/test_files/oryzabase_genes/OryzabaseGeneListEn_20140621.txt'

oryzabase_file = '/home/venkatesan/workspace/argoLD_project/data/oryzabase/OryzabaseGeneListEn_20141206.txt'


'''
AraCyc
'''
aracyc_test_file = '/home/venkatesan/workspace/explore/test_files/tair/aracyc/aracyc_pathways.20130709.txt'

aracyc_file = '/home/venkatesan/workspace/argoLD_project/data/tair/aracyc/aracyc_pathways.20130709'

'''
RiceCyc
'''
ricecyc_test_dir = '/home/venkatesan/workspace/explore/test_files/gramene_ricecyc/*.tab'

ricecyc_file = '/media/sf_F_DRIVE/IBC/argoLD_project/data/ricecyc/*.tab'

'''
OryzaTagLine input
'''
otl_test_inputfile = '/home/venkatesan/workspace/explore/test_files/oryzaTagLine/OTL_export_pheno+trait.csv'

otl_inputfile = '/media/sf_F_DRIVE/IBC/argoLD_project/data/southgreen/oryzatagline/OTL_export_pheno+trait.csv'

'''
UniProt input
'''
up_test_dir = '/home/venkatesan/workspace/explore/test_files/uniprot/*.dat'

up_dir = '/media/sf_F_DRIVE/IBC/argoLD_project/data/uniport/*.dat'
#########################################################
'''
 Output file path - RDF files - turtle syntax
'''
'''
 Ontology association files
''' 
prot_test_output = '/home/venkatesan/workspace/explore/rdf_ttl/associations_ttl/protein_associations.ttl'
gene_test_output = '/home/venkatesan/workspace/explore/rdf_ttl/associations_ttl/gene_associations.ttl'
qtl_test_output = '/home/venkatesan/workspace/explore/rdf_ttl/associations_ttl/qtl_associations.ttl'

protein_assoc_ttl = '/media/sf_F_DRIVE/IBC/argoLD_project/rdf/ontology_associations_ttl/protein_ttl/protein_associations.ttl'
gene_assoc_ttl = '/home/venkatesan/workspace/argoLD_project/rdf/ontology_associations_ttl/gene_ttl/gene_associations.ttl'
qtl_assoc_ttl = '/media/sf_F_DRIVE/IBC/argoLD_project/rdf/ontology_associations_ttl/qtl_ttl/qtl_associations.ttl' 

'''
Gramene genes/QTL files
'''
genomes_rdf_test_out = '/home/venkatesan/workspace/explore/rdf_ttl/gramene_genome_ttl/'
gramene_qtl_test_out = '/home/venkatesan/workspace/explore/rdf_ttl/gramene_qtl_ttl/'

gramene_genes_out = '/media/sf_F_DRIVE/IBC/argoLD_project/rdf/gramene_genes_ttl/'
gramene_qtl_out = '/media/sf_F_DRIVE/IBC/argoLD_project/rdf/gramene_qtl_ttl/'
'''
OryzaBaseDB file
''' 
oryzaBase_test_output = '/home/venkatesan/workspace/explore/rdf_ttl/oryzabase_ttl/oryzabase_genes.ttl'

oryzaBase_output = '/home/venkatesan/workspace/argoLD_project/rdf/oryzabase_ttl/oryzabase_genes.ttl'
#oryzaBase_output = '/home/venkatesan/Documents/data_samples/oryzabase/oryzabase_genes.ttl'

'''
AraCyc
'''
aracyc_test_output = '/home/venkatesan/workspace/explore/rdf_ttl/tair/aracyc/'

aracyc_output = '/home/venkatesan/workspace/argoLD_project/rdf/tair_ttl/aracyc_ttl/'

'''
RiceCyc
'''
ricecyc_test_output = '/home/venkatesan/workspace/explore/rdf_ttl/gramene_ricecyc_ttl/'

ricecyc_output = '/media/sf_F_DRIVE/IBC/argoLD_project/rdf/cyc_ttl/'

'''
OryzaTagLine 
'''
otl_test_output = '/home/venkatesan/workspace/explore/rdf_ttl/southgreen/oryzaTagLine/otl.ttl'

otl_output = '/media/sf_F_DRIVE/IBC/argoLD_project/rdf/southgreen/oryzatagline_ttl/otl.ttl'

'''
UniprotKB
'''
up_test_output = '/home/venkatesan/workspace/explore/rdf_ttl/uniprot_ttl/'

up_output = '/media/sf_F_DRIVE/IBC/argoLD_project/rdf/uniprot_ttl/'



pp = pprint.PrettyPrinter(indent=4)

#prot_gaf_files = glob.glob(protein_assoc_dir) #gene_assoc_dir stores file names(with the full path) as a list
#prot_gaf_files = glob.glob(prot_assoc_test_dir) #gene_assoc_dir stores file names(with the full path) as a list
#gene_gaf_files = glob.glob(gene_assoc_dir)
#gene_gaf_files = glob.glob(gene_assoc_input_dir)
#qtl_gaf_files = glob.glob(qtl_assoc_dir)

#goa = gafToRDF

#print "************** Protein-ontology associations *************\n"
#mapping = goa.gafEcoMap(eco_map_file)
#goa.allGafRDF(prot_gaf_files, prot_output_file, 'qtl') #
#goa.ProteinGafRDF(prot_gaf_files, mapping, protein_assoc_ttl) # allGafRDF(prot_gaf_files, protein_assoc_ttl, 'protein')
#print "************** Protein-ontology associations RDF converted *************\n\n"

#print "************** QTL-ontology associations *************\n"
#goa.allGafRDF(qtl_gaf_files, mapping, qtl_assoc_ttl, 'qtl')
#print "************** QTL-ontology associations RDF converted *************\n\n"

#gaf_eco_file = '/home/venkatesan/workspace/explore/test_files/ontology_associations/gaf-eco-mapping.txt'
#goa = gafToRDF
#ds = goa.gafEcoMap(gaf_eco_file)
#pp.pprint(mapping)

#gramene_genomes = glob.glob(gramene_genes_files) #gramene_genes_files
#g_parse = grameneParsers#oryzaBaseParser
#print "***************** Gramene Genes data ********************\n"
#g_parse = grameneParsers
#input_f = '/home/venkatesan/workspace/explore/test_files/gramene_genes/Oryza_brachyantha.txt' #Oryza_barthii.txt' Oryza_sativa_japonica
#geneHash = g_parse.geneParser(gramene_genomes)#grameneQTLRDF(gramene_qtl_dir, gramene_qtl_out) oryzaBaseRDF(oryzabase_file, oryzaBase_output) grameneGeneRDF(gramene_genomes, gramene_genes_out)
#pp.pprint(geneHash)
#g_parse.grameneGeneRDF(gramene_genomes, gramene_genes_out)
#print "********************************************************\n\n"

#print "*************** Gramene QTL data ***************\n"
#g_parse.grameneQTLRDF(gramene_qtl_file, gramene_qtl_out) #gramene_qtl_file, gramene_qtl_out  gramene_qtl_test_file, gramene_qtl_test_out
#print "***********************************************\n\n"

#print "*********** Cyc data *************\n"
#pw_files = glob.glob(ricecyc_file)
#ricecyc_ds = g_parse.CycParser(pw_files) # ricecyc_test_file ricecyc_file
#pp.pprint(ricecyc_ds)
#g_parse.CycRDF(ricecyc_ds, ricecyc_output) #ricecyc_test_output ricecyc_output
#print "*************************************\n\n"


#print "************ OryzaTagLine data **************\n"
#southG = southGreenParsers
#oryzaTag_ds = southG.otlParser(otl_inputfile) #otl_test_inputfile otl_inputfile
#pp.pprint(oryzaTag_ds)
#southG.otlRDF(oryzaTag_ds, otl_output)#otl_test_output otl_output
#print "********************************************\n\n"

# UniPort********
#up_files = glob.glob(up_dir)#up_dir up_test_dir
#print "*********** Uniprot data *************\n"
#up_converter = uniprotToRDF
#up_converter.upToRDF(up_files, up_output)#up_output up_test_output
#print "********************************************\n\n"


##print "************ TropGene data **************\n"
#path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/tropGene/rice.csv'     # The input
#path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/tropGene_ttl/tropgene.rice.ttl' # The output
#ds = TropgeneModel.tropGeneParser(path)# The parsing file withe tropGeneParser()
#pp.pprint(ds)    # For to see in teminal the parsing
#TropgeneModel.tropGeneToRDF(ds, path_output)  # The tranformation fonction tropGeneToRdf(input, output)


##print "************ OS.Japonica data orygene_db **************\n"
#path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/orygenes_db/os.japonicaCancat_test.gff3'    # The input
#path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/orygene_db_ttl/os.japonica.ttl' # The output
#ds = gffParser.parseGFF3(path)   # The parsing file withe tropGeneParser()
#pp.pprint(ds)    # For to see in teminal the parsing
#os_japonicaModel.os_indicaModeleRDF(ds, path_output)


##print "************ OS.Indica data orygene_db **************\n"
#path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/orygenes_db/os.indicaCancat_Test.gff3'    # The input
#path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/orygene_db_ttl/os.indica.ttl' # The output
#ds = gffParser.parseGFF3(path)   # The parsing file withe tropGeneParser()
#pp.pprint(ds)    # For to see in teminal the parsing
#os_indicaModel.os_indicaModeleRDF(ds, path_output)


##print "************ A.thaliana data orygene_db **************\n"
#path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/orygenes_db/a.thalianaCancat_test.gff3'    # The input
#path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/orygene_db_ttl/a.thaliana.ttl' # The output
#ds = gffParser.parseGFF3(path)
#pp.pprint(ds)    # For to see in teminal the parsing
#a_thalianaModel.a_thalianaModeleRDF(ds, path_output)


##print "************ Sniplay datasniplay_db **************\n"
#path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/sniplay_db/sniplay_test.txt'
#path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/sniplay_db_ttl/sniplay.ttl' # The output
sniplayPaserModel.sniplayPaserModel()

