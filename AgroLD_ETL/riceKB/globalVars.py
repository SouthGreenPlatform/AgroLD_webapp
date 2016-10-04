#!/usr/bin/env python
'''
This file contains all global variables i.e. URI patterns, Ontological terms and predicates to be used by the various parsers
 
'''

'''
TODO:
    1) Add variables for common predicates used in the Rdf converters
    2) Cleanup cleanup the script and add comments
'''
global taxon_ids, db_obj_type, base, pr, rdf, rdf_ns, rdfs_ns, rdfs_ns, owl, owl_ns, xsd, xsd_ns, base_uri, base_ns, base_vocab_uri, base_vocab_ns, sio_uri, sio_ns, obo_uri, obo_ns, ncbi_tax_uri,\
ncbi_tax_ns, uniprot, up_ns, gramene_gene, gr_g_ns, gramene_qtl, gr_qtl_ns, sio_term, go_aspects, ont_aspects, gene_term, protein_term, tigr_uri, tigr_ns, rapdb_uri, rapdb_ns,\
plant_trait_term,orygene_uri, orygene_ns, goa_uri, goa_ns, gr_assoc, gr_assoc_ns, tair_l_uri, tair_l_ns, met_pw_sio_term, ec_code_uri, ec_code_ns,reaction_uri, reaction_ns,\
pathway_uri, pathway_ns, otl_uri, otl_ns, plant_dev_term, plant_anatomy_term,germplasm_term, co_uri, co_ns, swo_uri, swo_ns, biocyc_pw_term, biocyc_react_term   

# Taxon - 'NCBI taxon IDs' : 'Taxon name' 
taxon_ids = {
         '4533' : 'Oryza brachyantha',
         '4538' : 'Oryza glaberrima',
         '4530' : 'Oryza sativa',
         '39947' : 'Oryza sativa japonica',
         '39946' : 'Oryza sativa indica',
         '65489' : 'Oryza barthii',
#         '40148' : 'Oryza_glumaepatula',
         '40149' : 'Oryza meridionalis',
#         '4536' : 'Oryza_nivara',
#         '4537' : 'Oryza_punctata',
#         '4529' : 'Oryza_rufipogon3s',
         '3702' : 'Arabidopsis thaliana',
         '4577' : 'Zea mays',
         '4558' : 'Sorghum bicolor',
         '4565' : 'Triticum aestivum',
         '4572' : 'Triticum urartu'
         }
# Resolvable URIs
db_obj_type = {
              'protein' : 'http://www.identifiers.org/uniprot/', # Note: assumes protein accessions are from UniProt
              'gene' : 'http://www.identifiers.org/gramene.gene/', # Note: assumes gene accessions are Gramene internal IDs e.g. GR:xxxxx
              'QTL' : 'http://www.identifiers.org/gramene.qtl/' # Note: assumes QTL accessions are Gramene QTL IDs e.g. AQED049
              }

# Prefixes
base = '@base'
base_uri = 'http://www.southgreen.fr/agrold/'
base_ns = 'agrold:'

pr = '@prefix'

rdf = 'http://www.w3.org/1999/02/22-rdf-syntax-ns#'
rdf_ns = 'rdf:'

rdfs = 'http://www.w3.org/2000/01/rdf-schema#'
rdfs_ns = 'rdfs:'

owl = 'http://www.w3.org/2002/07/owl#'
owl_ns = 'owl:'

xsd = 'http://www.w3.org/2001/XMLSchema#'
xsd_ns = 'xsd:'


owl_uri = 'http://www.w3.org/2002/07/owl#'
owl_ns = 'owl:'


#Internal URI/namespaces
#base_uri = 'http://www.southgreen.fr/agrold/'
#base_ns = 'agrold:'

base_vocab_uri = 'http://www.southgreen.fr/agrold/vocabulary/'
base_vocab_ns = 'agrold_vocabulary:'
#ont_uri = 'http://purl.obolibrary.org/obo/' # Ontology term URI

# Datasource specific URIs
ncbi_gene_uri = 'http://identifiers.org/ncbigene/'
ncbi_gene_ns = 'ncbigene:'

ncbi_tax_uri = 'http://purl.obolibrary.org/obo/NCBITaxon_'
ncbi_tax_ns = 'taxon:'

#uniprot = 'http://www.identifiers.org/uniprot/'
uniprot = 'http://purl.uniprot.org/uniprot/'
up_ns = 'uniprot:'

gramene_gene =  'http://www.identifiers.org/gramene.gene/'
gr_g_ns = 'gramene_gene:'

resource = 'http://www.southgreen.fr/agrold/resource/'
res_ns = 'agrold_schema:'

gramene_qtl = 'http://www.identifiers.org/gramene.qtl/'
gr_qtl_ns = 'gramene_qtl:' 

ensembl_plant = 'http://identifiers.org/ensembl.plant/'
ensembl_ns = 'ensembl:'

tair_l_uri = 'http://identifiers.org/tair.locus/'
tair_l_ns = 'tairlocus:'

tigr_uri = 'http://www.southgreen.fr/agrold/tigr.locus/'
tigr_ns = 'tigr:'

tigr_g_uri = 'http://identifiers.org/ricegap/'
tigr_g_ns = 'tigr_gene:'

rapdb_uri = 'http://www.southgreen.fr/agrold/rapdb/'
rapdb_ns = 'rapdb:'

orygene_uri = 'http://identifiers.org/oryzabase.gene/'
orygene_ns = 'oryzabase:'

ec_code_uri = 'http://identifiers.org/ec-code/'
ec_code_ns = 'ec:'

reaction_uri = 'http://www.southgreen.fr/agrold/biocyc.reaction/'
reaction_ns = 'reaction:'

pathway_uri = 'http://www.southgreen.fr/agrold/biocyc.pathway/'
pathway_ns = 'pathway:'

#BioCyc
swo_uri = 'http://edamontology.org/'
swo_ns = 'swo:'

# AraCyc
#aracyc_uri = 'http://www.southgreen.fr/agrold/aracyc.pathway/'
#aracyc_ns = 'aracyc_pathway:'
#aracyc_gene_uri = 'http://www.southgreen.fr/agrold/aracyc.gene/'
#aracyc_gene_ns = 'aracyc_gene:'
#aracyc_prot_uri = 'http://www.southgreen.fr/agrold/aracyc.protein/'
#aracyc_prot_ns = 'aracyc_protein:'

#RiceCyc
#ricecyc_uri = 'http://www.southgreen.fr/agrold/ricecyc.pathway/'
#ricecyc_ns = 'ricecyc_pathway:'

# SouthGreen
#----------------
# OTL
otl_uri = 'http://identifiers.org/otl/'
otl_ns = 'otl:'

#Orygenesdb
mirbase_uri = 'http://www.identifiers.org/mirbase/'
mirbase_ns = 'mirbase:'

mirbase_mature_uri = 'http://www.identifiers.org/mirbase.mature/'
mirbase_mature_ns = 'mature:'

protein_uri = 'http://www.southgreen.fr/agrold/orygenesdb.protein/'
protein_ns = 'protein:'

flanking_uri = 'http://www.southgreen.fr/agrold/orygenesdb.flankingregion/'
flanking_ns = 'flanking:'

mRNA_uri = 'http://www.southgreen.fr/agrold/orygenesdb.mrna/'
mRNA_ns = 'mrna:'

cDNA_uri = 'http://www.southgreen.fr/agrold/orygenesdb.cdna/'
cDNA_ns = 'cdna:'

otl_public_plante_uri = 'http://www.identifiers.org/otl/'
otl_public_plante_ns = 'otl_plante:'

OrygenesDB_uri = 'http://www.southgreen.fr/agrold/orygenesdb/'
OrygenesDB_ns = 'orygenesdb:'

chromosome_uri = 'http://www.southgreen.fr/agrold/chromosome/'
chromosome_ns = 'chromosome:'

embl_uri = 'http://identifiers.org/ena.embl/'
embl_ns = 'ena.embl:'

marker_uri = 'http://www.southgreen.fr/agrold/orygenesdb.marker/'
marker_ns = 'marker:'



#TROPGENE

study_uri = 'http://www.southgreen.fr/agrold/tropgene.study/'
study_ns = 'study:'

population_uri = 'http://www.southgreen.fr/agrold/tropgene.population/'
population_ns = 'population:'

qtl_uri = 'http://www.southgreen.fr/agrold/tropgene.qtl/'
qtl_ns = 'qtl:'

edam_uri ='http://edamontology.org/'
edam_ns = 'edam:'

trait_uri = 'http://www.southgreen.fr/agrold/tropgene.trait/'
trait_ns = 'trait:'

mapfeature_uri = 'http://www.southgreen.fr/agrold/tropgene.mapfeature/'
mapfeature_ns = 'mapfeature:'



# Ontology terms and aspects
sio_term = 'SIO_000897' # SIO term - association
gene_term = 'SO_0000704' # SO term - gene
mrna_term = 'SO_0000234' # SO term - mrna
protein_term = 'SO_0000104' # SO term - protein
qtl_term = 'SO_0000771' # SO term - qtl
plant_trait_term = 'TO_0000387' # TO term - plant trait (root term)
plant_dev_term = 'PO_0009012' # PO term - plant_structure_development_stage (root term)
plant_anatomy_term = 'PO_0025131' # PO term - plant_anatomy (root term)
germplasm_term = 'CO_715:0000225' # CO term - Passport information entity
met_pw_sio_term = 'SIO_010532' # SIO term - metabolic pathway
biocyc_pw_term = 'data_1157' # Pathway ID (BioCyc)
biocyc_react_term = 'data_2106' # Reaction ID (BioCyc)

co_uri = 'http://www.cropontology.org/rdf/'
co_ns = 'co:'

sio_uri = 'http://semanticscience.org/resource/' # association URI
sio_ns = 'sio:'

obo_uri = 'http://purl.obolibrary.org/obo/'
obo_ns = 'obo:'

#goa_uri = 'http://www.southgreen.fr/agrold/go.association/'
goa_uri = 'http://identifiers.org/goa/' 
goa_ns = 'goa:'

gr_assoc = 'http://www.southgreen.fr/agrold/gramene.association/'
gr_assoc_ns = 'gramene_association:'



go_aspects = {           
           # GO aspects
           'P' : 'BFO_0000056', # participates_in -  Biological process
           'F' : 'BFO_0000085', # has_function -  Molecular Function
           'C' :  'BFO_0000082' # located_in - Cellular Component
           }
ont_aspects = {
           # GO aspects
           'P' : 'participates_in', # participates_in - BFO_0000056  Biological process
           'F' : 'has_function', # has_function - BFO_0000085  Molecular Function
           'C' :  'located_in', # located_in - BFO_0000082 Cellular Component           
           # PO aspects - temporary, suffixed with agrold: relative URI  
           'A' : 'expressed_in',
           'G' : 'expressed_at',
           # TO aspect - temporary
           'T' : 'has_trait',
           # EO aspects - temporary
#           'E' : 'has_condition'
           'E' : 'observed_in'
           }


#sniplay
sniplay_gene_uri = 'http://www.identifiers.org/ricegap/'
sniplay_gene_ns = 'ricegap:'

sniplay_gene_integenic_uri =  'http://www.southgreen.fr/agrold/sniplay.gene/'
sniplay_gene_integenic_ns = 'sniplay_gene:'

sniplay_individual_uri =  'http://www.southgreen.fr/agrold/sniplay.individual/'
sniplay_individual_ns = 'sniplay_individual:'

sniplay_allele_uri = 'http://www.southgreen.fr/agrold/sniplay.allele/'
sniplay_allele_ns = 'allele:'

sniplay_pos_uri =  'http://www.southgreen.fr/agrold/sniplay.pos/'
sniplay_pos_ns = 'sniplay_pos:'

sniplay_consequence_uri =  'http://www.southgreen.fr/agrold/sniplay.consequence/'
sniplay_consequence_ns = 'consequence:'


#Brapi urgi phenotype
observationUnitDbId_uri = 'http://www.southgreen.fr/agrold/phenotype.observationUnitDbId/'
observationUnitDbId_ns = 'observationUnitDbId:'

studyDbId_uri = 'http://www.southgreen.fr/agrold/phenotype.studyDbId/'
studyDbId_ns = 'studyDbId:'

treatment_uri = 'http://www.southgreen.fr/agrold/phenotype.treatment/'
treatment_ns = 'treatment:'

observationUnitLevelLabel_uri = 'http://www.southgreen.fr/agrold/phenotype.observationLabel/'
observationUnitLevelLabel_ns = 'observation_level_label:'

observationVariableDbId_uri = 'http://www.southgreen.fr/agrold/phenotype.observationData/'
observationVariableDbId_ns = 'observation_data:'


#Wine dataset file name :

repeatmasker_uri = 'http://www.southgreen.fr/agrold/winedb.repeatmasker/'
repeatmasker_ns = 'repeatmasker:'