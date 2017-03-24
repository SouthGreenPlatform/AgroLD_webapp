import sys
print sys.path
from globalVars import *
from globalVars import base_vocab_ns
from gffParser import *
import pprint
import re
import os

__author__ = 'elhassouni'


def wineModeleRDF(wine_ds, output_file):
  # The differentes variable declaration
    wine_buffer = ''    # initilised the buffer at zero
    number_match_part_sbgi = 0
    number_match_part_kome = 0
    number_exon = 0
    number_cds = 0
    line_number = 0
    number_five_prime_UTR = 0
    number_three_prime_UTR = 0
    rdf_writer = open(output_file, "w")
    chromosome_list = list()
    otl_plante = list()

# The first wrinting in the file is the prefix


    rdf_writer.write(base + "\t" + "<" + base_uri + "> .\n")
    rdf_writer.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    rdf_writer.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    rdf_writer.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    rdf_writer.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    rdf_writer.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    rdf_writer.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    rdf_writer.write(pr + "\t" + chromosome_ns + "<" + chromosome_uri + "> .\n")
    rdf_writer.write(pr + "\t" + repeatmasker_ns + "<" + repeatmasker_uri + "> .\n")

 # In here we buil the modele and writer in file with ttl format

    for records in wine_ds:
            line_number+=1
            # Chromosome triple
            if not records['seqid'] in chromosome_list:
                wine_buffer = ''
                chromosome_list.append(records['seqid'])
                wine_buffer += chromosome_ns + re.sub('chr', '', records['seqid']) + "\n"
                wine_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "29760" + " ;\n"
                wine_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                wine_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000430" + " .\n"
                print(wine_buffer)
                rdf_writer.write(wine_buffer)
    
    # wine.gff3 for RepeatMasker
    
            if records['source'] == "RepeatMasker":
                wine_buffer = ''
                wine_buffer += repeatmasker_ns + records['seqid'] + '_' + str(records['start']) + "\n"
                wine_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                wine_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                wine_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO:0000658" + " ;\n"
                wine_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "29760" + " ;\n"
                wine_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                wine_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                wine_buffer += "\t" + base_vocab_ns + "has_target" + "\t" + " \"" + records['attributes']['Target'] + "\" " + " ;\n"
                wine_buffer += "\t" + base_vocab_ns + "has_strand" + "\t" + " \"" + records['strand'] + "\" " +  " ;\n"
                wine_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + chromosome_ns + re.sub('chr', '', records['seqid']) + " .\n"
                print(wine_buffer)
                rdf_writer.write(wine_buffer)



path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/plant_breeding_db/wine_db/florendovirus_Vitis_12X_V0.gff3'    # The input
path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/Wine.ttl' # The output
ds = parseGFF3(path)
pp.pprint(ds)   # For to see in teminal the parsing
wineModeleRDF(ds, path_output)