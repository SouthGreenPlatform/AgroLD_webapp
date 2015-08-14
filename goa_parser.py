#!/usr/bin/env python


import glob
import re
import Bio.UniProt.GOA

#my_file = open('/home/venkatesan/workspace/explore/test_files/O_sativa_sample.goa', "r")
#my_file = open('/home/venkatesan/workspace/explore/test_files/po_protein_association_sample.txt', "r")
#mydir = os.listdir('/home/venkatesan/workspace/explore/test_files/')

# Input directory path
mydir = '/home/venkatesan/workspace/explore/test_files/go_association/*.*'
files = glob.glob(mydir) # stores file names(with the full path) as a list

# Output file path - RDF files - turtle syntax 
output_path = '/home/venkatesan/workspace/explore/output/go_association.ttl'
output_file = open(output_path, "w") 

# Taxon - 'NCBI taxon IDs' : 'Taxon name' 
taxon_ids = {
         '4533' : 'Oryza brachyantha',
         '4538' : 'Oryza glaberrima',
         '4530' : 'Oryza sativa',
         '39947' : 'Oryza sativa japonica',
         '39946' : 'Oryza sativa indica'
         }
# Resolvable URIs
db_obj_type = {
              'protein' : 'http://www.identifiers.org/uniprot/', # Note: assumes protein accessions are from UniProt
              'gene' : 'http://www.identifiers.org/gramene.gene/', # Note: assumes gene accessions are Gramene internal IDs e.g. GR:xxxxx
              'QTL' : 'http://www.identifiers.org/gramene.qtl/' # Note: assumes QTL accessions are Gramene QTL IDs e.g. AQED049
              }

ont_uri = 'http://purl.obolibrary.org/obo/' # Ontology term URI


sio_uri = 'http://semanticscience.org/resource/' 
sio_ns = 'sio:'
sio_term = 'SIO_000897' # SIO term - association
# Prefixes
pr = '@prefix'

rdf = 'http://www.w3.org/1999/02/22-rdf-syntax-ns#'
rdf_ns = 'rdf:'

rdfs = 'http://www.w3.org/2000/01/rdf-schema#'
rdfs_ns = 'rdfs:'

base_uri = 'http://www.southgreen.fr/riceknowledgebase/'
base_ns = 'rkb:'

obo_uri = 'http://purl.obolibrary.org/obo/'
obo_ns = 'obo:'

ncbi_tax_uri = 'http://purl.obolibrary.org/obo/NCBITaxon_'
ncbi_tax_ns = 'taxon:'

uniprot = 'http://www.identifiers.org/uniprot/'
up_ns = 'uniprot:'

gramene_gene =  'http://www.identifiers.org/gramene.gene/'
gr_g_ns = 'gramene_gene:'

gramene_qtl = 'http://www.identifiers.org/gramene.qtl/'
gr_qtl_ns = 'gramene_qtl:' 

#BFO URIs
p_bfo_uri = 'http://purl.obolibrary.org/obo/BFO_0000056'
p_ns = 'participates_in:'
f_bfo_uri = 'http://purl.obolibrary.org/obo/BFO_0000085'
f_ns = 'has_function:'
c_bfo_uri = 'http://purl.obolibrary.org/obo/BFO_0000082'
c_ns = 'located_in:'

# predicates according to the aspect
go_aspects = {           
           # GO aspects
           'P' : 'BFO_0000056', # participates_in -  Biological process
           'F' : 'BFO_0000085', # has_function -  Molecular Function
           'C' :  'BFO_0000082' # located_in - Cellular Component
           }
ont_aspects = {           
           # PO aspects - temporary  
           'A' : 'expressed_in', # suffixed with rkb: relative URI
           'G' : 'expressed_at', # suffixed with rkb: relative URI
           # TO aspect - temporary
           'T' : 'has_trait' # suffixed with rkb: relative URI
           }


def goaRDF(files, output_file, flag, tag=True):
    flag.lower()
    assoc_line = 0
    rdf_buffer = '' 
    previous_obj_id = ''
    # Printing prefixes
    output_file.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    output_file.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    output_file.write(pr + "\t" + base_ns + "<" + base_uri + "> .\n")
    output_file.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    output_file.write(pr + "\t" + sio_ns + "<" + sio_uri + "> .\n")
    output_file.write(pr + "\t" + ncbi_tax_ns + "<" + ncbi_tax_uri + "> .\n\n")
    if flag == 'protein':
        output_file.write(pr + "\t" + up_ns + "<" + uniprot + "> .\n\n")
    if flag == 'gene':
        output_file.write(pr + "\t" + gr_g_ns + "<" + gramene_gene + "> .\n\n")
    if flag == 'qtl':
        output_file.write(pr + "\t" + gr_g_ns + "<" + gramene_gene + "> .\n\n")
      
    for file_path in files:
        handle = open(file_path, "r")
        goa = Bio.UniProt.GOA.gafiterator(handle)
        uniq_obj_id = {}
        for assoc in goa:
            assoc_line += 1
            ont_term = assoc['GO_ID'].replace(":", "_")
            taxon = ''.join(assoc['Taxon_ID'])
            tax_id = taxon.lstrip('taxon:')
            current_obj_id = assoc['DB_Object_ID']
            # Flush
            if previous_obj_id and current_obj_id not in previous_obj_id:
                rdf_buffer = re.sub(' ;$', ' .', rdf_buffer)
                output_file.write(rdf_buffer)
                rdf_buffer = ''
                
            if current_obj_id not in uniq_obj_id:
                if flag == 'protein':
                    rdf_buffer += up_ns + current_obj_id + "\n"
                if flag == 'gene':
                    rdf_buffer += gr_g_ns + current_obj_id + "\n"
                if flag == 'qtl':
                    rdf_buffer += gr_qtl_ns + current_obj_id + "\n"
                rdf_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (assoc['DB_Object_Symbol']) + " ;\n"
                rdf_buffer += "\t" + base_ns + "description" + "\t" + '"%s"' % (assoc['DB_Object_Name']) + " ;\n"
                for synonym in assoc['Synonym']:
                    rdf_buffer += "\t" + base_ns + "synonym" + "\t" + '"%s"' % (synonym) + " ;\n"
                rdf_buffer += "\t" + base_ns + "taxon" + "\t" + ncbi_tax_ns + tax_id + " ;\n"
                uniq_obj_id[current_obj_id] = 1
                previous_obj_id = current_obj_id
            # Reification        
            output_file.write(base_ns + "triple_" + current_obj_id + "_" + ont_term + "_" + str(assoc_line) + "\n")
            output_file.write("\t" + rdf_ns + "type" + "\t" + rdf_ns +"Statement" + " ;\n")
            output_file.write("\t" + rdfs_ns + "subClassOf" + "\t" + sio_ns + sio_term + " ;\n") 
            output_file.write("\t" + rdf_ns + "subject" + "\t" + up_ns + current_obj_id + " ;\n")
            if tag:
                output_file.write("\t" + rdf_ns + "predicate" + "\t" + obo_ns + go_aspects[assoc['Aspect']] + " ;\n")
            else:
                output_file.write("\t" + rdf_ns + "predicate" + "\t" + base_ns + ont_aspects[assoc['Aspect']] + " ;\n")
            output_file.write("\t" + rdf_ns + "object" + "\t" + obo_ns + ont_term + " ;\n")
            output_file.write("\t" + base_ns + "evidence" + "\t" + '"%s"' % (assoc['Evidence']) + " ;\n")
            output_file.write("\t" + base_ns + "assigned_by" + "\t" + '"%s"' % (assoc['Assigned_By']) + " ;\n")
            output_file.write("\t" + base_ns + "date" + "\t" + '"%s"' % (assoc['Date']) + " .\n")
            
            # Flushing                
            if current_obj_id == previous_obj_id:
                if tag:
                    rdf_buffer += "\t" + obo_ns + go_aspects[assoc['Aspect']] + "\t" + obo_ns + ont_term + " ;\n"
                else:
                    rdf_buffer += "\t" + base_ns + ont_aspects[assoc['Aspect']] + "\t" + obo_ns + ont_term + " ;\n"
            previous_obj_id = current_obj_id
        #Last Flush
    if previous_obj_id:
        rdf_buffer = re.sub(' ;$', ' .', rdf_buffer) 
        output_file.write(rdf_buffer)
    handle.close()
    # end of if flag == 'protein'

