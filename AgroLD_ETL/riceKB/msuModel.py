import sys
print sys.path
from globalVars import *
from globalVars import base_vocab_ns
from gffParser import *
import pprint
import re
import os
import pandas as pd
import numpy as np
'''
Created on May, 2017
The msuModel module is created as part of the Rice Knowledge Base project.

This module contains Parsers, RDF converters and generic functions for handling MSU data


@author: larmande
'''
__author__  = "larmande"

#TODO 1) Add documentation
#TODO 2) needs code Refactoring
#TODO 3) better Error handling


def geneParser(infile):
    #    pp = pprint.PrettyPrinter(indent=4)
    gene_hash = {}
    tigr_pattern = re.compile(r'^LOC\_Os\d{1,2}g\d{5}\.\d$')
    rap_pattern = re.compile(r'^Os\d{2}g\d{7}$')
    array = pd.read_csv(infile, sep="\t", delimiter=None, dtype='str')
    #array['locus_id'].replace('', np.nan, inplace=True)
    #array.dropna(subset=['locus_id'], inplace=True)
    print array
    return array

def msuModeleRDF(msu_ds, output_file):
  # The differentes variable declaration
    os_japonica_buffer = ''    # initilised the buffer at zero
    number_match_part_sbgi = 0
    number_match_part_kome = 0
    number_exon = 0
    number_cds = 0
    line_number = 0
    number_five_prime_UTR = 0
    number_three_prime_UTR = 0
    rdf_writer = open(output_file, "w")
    chromosome_list = list()
    gene_list = list()

# The first wrinting in the file is the prefix


    print ("*************msu RDF conversion begins***********\n")
    rdf_writer.write(base + "\t" + "<" + base_uri + "> .\n")
    rdf_writer.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    rdf_writer.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    rdf_writer.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    rdf_writer.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    rdf_writer.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    rdf_writer.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    rdf_writer.write(pr + "\t" + chromosome_ns + "<" + chromosome_uri + "> .\n")
    rdf_writer.write(pr + "\t" + interpro_ns + "<" + interpro_uri + "> .\n")
    rdf_writer.write(pr + "\t" + msu_ns + "<" + msu_uri + "> .\n")

    # Ajout du prefix pour la realese des donnees
    rdf_writer.write(pr + "\t" + res_ns + "<" + resource + "> .\n\n")

# In here we buil the modele and writer in file with ttl format

    for records in msu_ds.as_matrix(columns=None):
        line_number+=1
        # Chromosome triple
        if not records[0] in chromosome_list:
            os_japonica_buffer = ''
            chromosome_list.append(records[0])
            os_japonica_buffer += chromosome_ns + re.sub('Os|Chr', '', records[0]) + "\n"
            os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
            os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Chromosome" + " ;\n"
            os_japonica_buffer += "\t" + base_vocab_ns + "genome_assembly_name" + "\t" +  "\"Os-Nipponbare-Reference-IRGSP-1.0\"" + " ;\n"
            os_japonica_buffer += "\t" + base_vocab_ns + "genome_assembly_version" + "\t"  + "\"7\"" + " ;\n"
           # os_japonica_buffer += "\t" + base_vocab_ns + "type" + "\t" + res_ns + "Chromosome" + " ;\n\n"
            os_japonica_buffer = re.sub(' ;$', ' .\n', os_japonica_buffer)
            rdf_writer.write(os_japonica_buffer)
            print(os_japonica_buffer)

#msu.gff3

        if len(records) is  10:
            if not records[1] in gene_list:
                os_japonica_buffer = ''
                gene_list.append(records[1])
                os_japonica_buffer += msu_ns + records[1] + "\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + 'IRGSP-1.0' + "\" ;\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Gene" + " ;\n"
                # os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records[1] + "\" ;\n"
                # os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000704" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_annotation" + "\t" + " \"" + records[9] + "\" ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(
                records[3]) + "\"^^xsd:integer ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(
                records[4]) + "\"^^xsd:integer ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub(
                'Os|Chr',
                '',
                records[
                    0]) + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "strand" + "\t" + " \"" + records[5] + "\"^^xsd:string ;\n"
                if records[6] == "Y":
                    os_japonica_buffer += "\t" + base_vocab_ns + "is_TE" + "\t" + "\"true\"^^xsd:boolean ;\n"
                else:
                    os_japonica_buffer += "\t" + base_vocab_ns + "is_TE" + "\t" + "\"false\"^^xsd:boolean ;\n"

                if records[7] == "Y":
                    os_japonica_buffer += "\t" + base_vocab_ns + "is_expressed" + "\t" + "\"true\"^^xsd:boolean ;\n"
                else:
                    os_japonica_buffer += "\t" + base_vocab_ns + "is_expressed" + "\t" + "\"false\"^^xsd:boolean ;\n"

                if records[8] == "Y":
                    os_japonica_buffer += "\t" + base_vocab_ns + "is_representative" + "\t" + "\"true\"^^xsd:boolean ;\n"
                else:
                    os_japonica_buffer += "\t" + base_vocab_ns + "is_representative" + "\t" + "\"false\"^^xsd:boolean ;\n"
                os_japonica_buffer = re.sub(' ;$', ' .\n', os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)
                print(os_japonica_buffer)
    print(line_number)


pp = pprint.PrettyPrinter(indent=4)

#TEST PARAM
#path = '/Users/plarmande/Downloads/IRGSP-1.0_representative/transcripts_mRNA.gff'
path_output = '/Users/plarmande/Downloads/all.locus_brief_info.7.ttl' # The output
#path = '/opt/TOS_DI-20141207_1530-V5.6.1/workspace/gff_data_orygeneDB/os_japonica/os_indicaCancat.gff3'    # The input
#path_output = '/home/elhassouni/Bureau/japonica.ttl' # The output
#ds = parseGFF3(path)   # The parsing file withe tropGeneParser()
#pp.pprint(ds)    # For to see in teminal the parsing

#ds = os_indicaModele(ds, path_output)  # The path_output)  # The tranformation fonction tropGeneToRdf(input, output)
ds = geneParser('/Users/plarmande/Downloads/all.locus_brief_info.7.txt')
msuModeleRDF(ds, path_output)
