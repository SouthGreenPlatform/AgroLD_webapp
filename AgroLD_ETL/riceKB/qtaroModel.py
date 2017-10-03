import sys
#print sys.path
from globalVars import *
from globalVars import base_vocab_ns
import pprint
import re
import os
import pandas as pd
import numpy as np
import urllib
import json

'''
Created on May, 2017
The QtaroParsers module is created as part of the Rice Knowledge Base project.

This module contains Parsers, RDF converters and generic functions for handling Qtaro data

TODO:
    1) Add documentation
    2) better Error handling
@author: larmande
'''
__author__  = "larmande"


# ogro_id;gene;gene_symbol;character_major;character_minor;chromosome;start;end;locus_id;browse;isolation;objective;doi


def geneParser(infile):
    #    pp = pprint.PrettyPrinter(indent=4)
    gene_hash = {}
    tigr_pattern = re.compile(r'^LOC\_Os\d{1,2}g\d{5}\.\d$')
    rap_pattern = re.compile(r'^Os\d{2}g\d{7}$')
    tair_pattern = re.compile(r'^AT[1-5]G\d{5}$')
    prot_pattern = re.compile(
        r'^([A-N,R-Z][0-9]([A-Z][A-Z, 0-9][A-Z, 0-9][0-9]){1,2})|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])(\.\d+)?$')
    ont_pattern = re.compile(r'^\w+\:\d{7}$')
    array = pd.read_csv(infile, sep=";", delimiter=None, dtype='str')
    array['locus_id'].replace('', np.nan, inplace=True)
    array.dropna(subset=['locus_id'], inplace=True)
    print array
    return array

#id|qtl_gene_name|character_major|character_minor|marker_volume|chromosome|start|end|source|lod|crossed_a|crossed_b|direction|references|ref_number|marker|marker_physical|marker_fine_1|marker_fine_2|marker_fine_3|marker_interval_1|marker_interval_2|marker_interval_3|marker_single|analytic_group|group_size|trait_name|contribution_rate|synergy_effect|publication_year

def qtlParser(infile):
    raw_headers = "id|qtl_gene_name|character_major|character_minor|marker_volume|chromosome|start|end|source|lod|crossed_a|crossed_b|direction|references|ref_number|marker|marker_physical|marker_fine_1|marker_fine_2|marker_fine_3|marker_interval_1|marker_interval_2|marker_interval_3|marker_single|analytic_group|group_size|trait_name|contribution_rate|synergy_effect|publication_year"
    headers = raw_headers.split("|")
    qtl_ds = list()

    #    pp = pprint.PrettyPrinter(indent=4)
    qtl_ds = pd.read_csv(infile, sep="|", delimiter=None, dtype='str')

    # fileHandle = open(infile, "r")
    # lines = fileHandle.readlines()
    # lines.pop(0)  # remove header
    # for line in lines:
    #     line = re.sub('\n$', '', line)
    #     items = line.split('\t')
    #     qtl_ds.append(dict(zip(headers, items)))
    #
    # fileHandle.close()
    print qtl_ds
    return qtl_ds


'''
 RDF Converters
'''
# ogro_id;gene;gene_symbol;character_major;character_minor;chromosome;start;end;locus_id;browse;isolation;objective;doi
def qtaroGeneRDF(infile, output_dir):
    gene_buffer = ''
    to_hash = dict()
    gene_counter = 0
    turtle_file_name = "qtaro.gene.ttl"
    outfile = os.path.join(output_dir, turtle_file_name)
    outHandle = open(outfile, "w")
    rap_pattern = re.compile(r'^Os\d{2}g\d{7}$')
    print "*********** Parsing Qtaro Gene data ***************\n"

    gene_ds = geneParser(infile)

    print "************* Qtaro Gene RDF conversion begins***********\n"

    outHandle.write(base + "\t" + "<" + base_uri + "> .\n")
    outHandle.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    outHandle.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    outHandle.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    outHandle.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    outHandle.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    outHandle.write(pr + "\t" + qtaro_gene_ns + "<" + qtaro_gene + "> .\n")
    outHandle.write(pr + "\t" + ensembl_ns + "<" + ensembl_plant + "> .\n")
    outHandle.write(pr + "\t" + dc_ns + "<" + dc_uri + "> .\n")
    '''
    Ajout du prefix pour la release des donnees
    '''
    outHandle.write(pr + "\t" + res_ns + "<" + resource + "> .\n\n")

    for records in gene_ds.as_matrix(columns=None):
        gene_buffer = ''
        gene_counter += 1

        # ogro_id;gene;gene_symbol;character_major;character_minor;chromosome;start;end;locus_id;browse;isolation;objective;doi
        print records
        if rap_pattern.match(records[8]) and isinstance(records[8],str):
            gene_buffer += ensembl_ns + records[8] + "\n"
            gene_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Gene" + " ;\n"
            gene_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (records[1]) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "has_symbol" + "\t" + '"%s"' % (records[2]) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t" + '"%s"' % (records[5]) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + '"%s"' % (records[6]) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + '"%s"' % (records[7]) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "has_trait" + "\t" + '"%s"' % (records[3]) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "has_trait" + "\t" + '"%s"' % (records[4]) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "has_dbxref" + "\t" + ensembl_ns + records[8] + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "has_dbxref" + "\t" + qtaro_gene_ns + records[0] + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "description" + "\t" + '"%s"' % (records[11]) + " ;\n"
            if records[12] is not np.nan:
                gene_buffer += "\t" + dc_ns + "identifier" + "\t" +  doi_ns + records[12] + " ;\n"
            gene_buffer = re.sub(' ;$', ' .\n', gene_buffer)
            outHandle.write(gene_buffer)
    outHandle.close()
    print "Number of Genes: %s\n" % (str(gene_counter))
    print "********* Qtaro GENE RDF completed ***********\n"

def qtaroQTLRDF(infile, output_dir):
    qtl_buffer = ''
    to_hash = dict()
    qtl_counter = 0
    turtle_file_name = "qtaro.qtl.ttl"
    outfile = os.path.join(output_dir, turtle_file_name)
    outHandle = open(outfile, "w")

    print "*********** Parsing Qtaro QTL data ***************\n"

    qtl_ds = qtlParser(infile)


    #    print "Gramene QTL data has been parsed!\n"
    #    print "*************************************\n"

    print "************* Qtaro QTL RDF conversion begins***********\n"

    outHandle.write(base + "\t" + "<" + base_uri + "> .\n")
    outHandle.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    outHandle.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    outHandle.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    outHandle.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    outHandle.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    outHandle.write(pr + "\t" + qtaro_qtl_ns + "<" + qtaro_qtl + "> .\n")

    '''
    Ajout du prefix pour la release des donnees
    '''
    outHandle.write(pr + "\t" + res_ns + "<" + resource + "> .\n\n")

    for records in qtl_ds.as_matrix(columns=None):
        qtl_buffer = ''
        qtl_counter += 1
        #chrm = records['Chromosome'].replace("Chr. ", "")
        #to_id = records['TOid'].replace(":", "_")
        print records
        qtl_buffer += qtaro_qtl_ns + records[0] + "\n"
        qtl_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "QTL" + " ;\n"
        # qtl_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
        # qtl_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + qtl_term + " ;\n"
        qtl_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (records[1]) + " ;\n"
        qtl_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t" + '"%s"' % (records[5]) + " ;\n"
        qtl_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + '"%s"' % (records[6]) + " ;\n"
        qtl_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + '"%s"' % (records[7]) + " ;\n"
        qtl_buffer += "\t" + base_vocab_ns + "has_trait" + "\t" + '"%s"' % (records[2]) + " ;\n"
        qtl_buffer += "\t" + base_vocab_ns + "has_trait" + "\t" + '"%s"' % (records[3]) + " ;\n"
        qtl_buffer += "\t" + base_vocab_ns + "has_trait" + "\t" + '"%s"' % (records[26]) + " ;\n"
        qtl_buffer += "\t" + base_vocab_ns + "lod" + "\t" + '"%s"' % (records[9]) + " ;\n"
        qtl_buffer += "\t" + base_vocab_ns + "date" + "\t" + '"%s"' % (records[29]) + " ;\n"

        #if records['marker_interval_1']

        #        if to_id not in to_hash:
        #            outHandle.write(obo_ns + to_id + "\n")
        #            outHandle.write("\t" + rdf_ns + "type" + "\t" + obo_ns + plant_trait_term + " ;\n") #base_vocab_ns + "Concept"
        #            outHandle.write("\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + plant_trait_term + " ;\n")
        #            outHandle.write("\t" + rdfs_ns + "label" + "\t" + '"%s"' % (records['TraitName']) + " ;\n")
        #            outHandle.write("\t" + base_vocab_ns + "has_symbol" + "\t" + '"%s"' % (records['TraitSymbol']) + " ;\n")
        #            outHandle.write("\t" + base_vocab_ns + "has_category" + "\t" + '"%s"' % (records['Category']) + " .\n")
        #            to_hash[to_id] = 1
        qtl_buffer = re.sub(' ;$', ' .\n', qtl_buffer)
        outHandle.write(qtl_buffer)
    outHandle.close()
    print "Number of QTLs: %s\n" % (str(qtl_counter))
    print "********* Qtaro QTL RDF completed ***********\n"

#geneParser('../test_files/qtaro/Qtaro-Gene-export.csv')

#qtaroGeneRDF('../test_files/qtaro/Qtaro-Gene-export.csv','.')
qtaroQTLRDF('../test_files/qtaro/qtaro.qtl.csv','.')