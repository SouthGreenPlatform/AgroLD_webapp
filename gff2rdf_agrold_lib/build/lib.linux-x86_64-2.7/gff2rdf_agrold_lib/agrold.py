#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
    Implémentation de la proclamation de la bonne parole.

    Usage:

     from sm_lib import proclamer
     proclamer()
"""
from __future__ import with_statement
import re
import urllib2
import json
import uuid
import fileinput
import time
from datetime import datetime
from collections import namedtuple
import gzip
import urllib
import pprint

__all__ = ['gff_parser', 'gff_model2rdf']

__author__ = 'elhassouni'

#prefixes
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
obo_uri = 'http://purl.obolibrary.org/obo/'
obo_ns = 'obo:'
base_vocab_uri = 'http://www.southgreen.fr/agrold/vocabulary/'
base_vocab_ns = 'agrold_vocabulary:'
chromosome_uri = 'http://www.southgreen.fr/agrold/chromosome/'
chromosome_ns = 'chromosome:'
resource = 'http://www.southgreen.fr/agrold/resource/'
res_ns = 'agrold_schema:'


#Initialized GeneInfo named tuple. Note: namedtuple is immutable
gffInfoFields = ["seqid", "source", "type", "start", "end", "score", "strand", "phase", "attributes"]
GFFRecord = namedtuple("GFFRecord", gffInfoFields)


def attributes_parser(attributeString):
    """
    Parse the GFF3 attribute column and return a dict

    :param attributeString: string of string descended from the 9 colomn
    :return: return a list of value and key
    """
    if attributeString == ".": return {}
    ret = {}
    for attribute in attributeString.split(";"):
        key, value = attribute.split("=")
        ret[urllib.unquote(key)] = urllib.unquote(value)
    return ret


def gff_parser(filename):
    """
    A minimalistic GFF3 format parser.
    Yields objects that contain info about a single GFF3 feature.
    Supports transparent gzip decompression.

    :param filename: the file that we need to parse
    :return: return a dictionary
    """
    map_ds = list()
    openFunc = gzip.open if filename.endswith(".gz") else open
    with openFunc(filename) as infile:
        for line in infile:
            if line.startswith("#"): continue
            parts = line.strip().split("\t")
            #If this fails, the file format is not standard-compatible
            assert len(parts) == len(gffInfoFields)
            #Normalize data
            normalizedInfo = {
                "seqid": None if parts[0] == "." else urllib.unquote(parts[0]),
                "source": None if parts[1] == "." else urllib.unquote(parts[1]),
                "type": None if parts[2] == "." else urllib.unquote(parts[2]),
                "start": None if parts[3] == "." else int(parts[3]),
                "end": None if parts[4] == "." else int(parts[4]),
                "score": None if parts[5] == "." else float(parts[5]),
                "strand": None if parts[6] == "." else urllib.unquote(parts[6]),
                "phase": None if parts[7] == "." else urllib.unquote(parts[7]),
                "attributes": attributes_parser(parts[8])
            }
            map_ds.append(normalizedInfo)
            #Alternatively, you can emit the dictionary here, if you need mutability:
            #    yield normalizedInfo
            #yield GFFRecord(**normalizedInfo)
        return map_ds


def alias_chromosome(alias_chromosome_file):
    """
    Sometimes seqid define a identifiant and contains the chromosome the best solution
    is to build alias_dico from of alias chromosome file who are write out by the user

    e.g:
        > in alias_file

        traes3bPseudomoleculeV1=3B

        > after alias_chromosome

        [{'alias': 'traes3bPseudomoleculeV1', 'chromosome': '3B'}]

    :param topGene_file: the path of alias_fil.txt
    :return: return a dictionary
    """
    map_reader = open(alias_chromosome_file, "r")
    headers = ['alias', 'chromosome']
    map_ds = list()
    lines = map_reader.readlines()
    for line in lines:
        line = re.sub('\n$', '', line)
        records = line.split('=') #('\t')
        map_ds.append(dict(zip(headers, records)))
    map_reader.close()
    return map_ds


def search_parent(parent, ds):
    """
    Search the type of parent in scouring the dictionary for found the parent and return the type of the parents found

    :param Parent:the parents who want to fund
    :param ds:the dictionary
    :return: return the type of feature of the parents
    """
    for records in ds:
        try:
            if records['attributes']['ID'] == parent:
                yield (records['type'])
        except:
            search_parent(parent, ds)


def search_procedure(parents, dico_ds):
    """
    The main function who search the parent of an id
    :param parents:the parents who want to fund
    :return:return the type of feature of the parents sended by search parent
    """
    for p in search_parent(parents, dico_ds):
        return p


def annotation(text):
    """
    The fonction get the NCBI annotation after submite the latin name of the species

    :param text: name of species that we need found the NCBI_taxon
    :return: return tne NCBI annotation
    """
    REST_URL = "http://data.agroportal.lirmm.fr"
    API_KEY = "45784a40-0248-46cb-aea8-c46014cde2d4"
    def get_json(url):
        opener = urllib2.build_opener()
        opener.addheaders = [('Authorization', 'apikey token=' + API_KEY)]
        return json.loads(opener.open(url).read())
    def return_annotations(annotations, get_class=True):
        for result in annotations:
            class_details = get_json(result["annotatedClass"]["links"]["self"]) if get_class else result["annotatedClass"]
        return class_details["@id"]
    annotations = get_json(REST_URL + "/annotator?text=" + urllib2.quote(text) +"&ontologies=EFO,NCBITAXON&longest_only=true")
    taxon = return_annotations(annotations)
    return taxon


def gff_model2rdf(path_gff_file, path_output_file, text_to_annotate, adress_uri, alias_chromosome_file):
    """
    The main fonction who translate the GFF file in RDF under ttl format

    :param path_gff_file: the file  that to be translate in RDF
    :param path_output_file: the rdf file
    :param text_to_annotate: name of species
    :param adress_uri: name of uri that the data will to to have
    :param alias_chromosome_file:
    :return: a message of execution or error
    """
    #pp = pprint.PrettyPrinter(indent=4)

    start_time = time.time()
    dico_ds = gff_parser(path_gff_file)
    alias_chromosome_dict = alias_chromosome(alias_chromosome_file)
    taxon_id = annotation(text_to_annotate)
    rdf_writer = open(path_output_file, "w")
    chromosome_list = list()
    listOfURI = list()
    for records in dico_ds:
        gff_buffer = ''
        if records['seqid'] not in chromosome_list:
            for alias_f in alias_chromosome_dict:
                if alias_f['alias'] == records['seqid']:
                    chromosome_list.append(records['seqid'])
                    gff_buffer += chromosome_ns + re.sub('Osi', '', alias_f['chromosome']) + "\n"
                    gff_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + taxon_id  + " ;\n"
                    gff_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Chromosome" + " .\n"
                    print(gff_buffer)
        try:
            gff_buffer += records['type'] + ":" + records['attributes']['ID'] + "\n"
        except LookupError:
            gff_buffer += records['type'] + ":" + records['attributes']['Name'] + "\n"
        except:
            RandomUUID = uuid.uuid4()
            gff_buffer += records['type'] + ":" + str(RandomUUID) + "\n"
        gff_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
        gff_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + records["type"] + " ;\n"
        gff_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + taxon_id  + " ;\n"
        gff_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
        gff_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
        if records['type'] not in listOfURI:
            listOfURI.append(records['type'])
        if 'attributes' in records:
            if records["type"] == "mRNA":
                gff_buffer += "\t" + base_vocab_ns + "develops_from" + "\t\t" + "gene:" + records['attributes']['Parent'] + " ;\n"
    #Pour les type de donnee polypeptide
            if records["type"] == "polypeptide":
                    gff_buffer += "\t" + base_vocab_ns + "derives_from" + "\t\t" + "mRNA:" + records['attributes']['Derives_from'] + " ; \n"
    #Pour les type de feature CDS et EXON et tout les autres feature
            if (records["type"] != 'mRNA') or (records["type"] != 'gene') or (records["type"] != 'polypeptide'):
                if 'Parent' in records['attributes']:
                    id = search_procedure(records['attributes']['Parent'], dico_ds)
                    gff_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + str(id) + ":" + records['attributes']['Parent'] + " ; \n"
        gff_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Osi', '', records['seqid']) + " .\n"
        print (gff_buffer)
        rdf_writer.write(gff_buffer)
    gff_prefix_buffer = ''
    gff_prefix_buffer += base + "\t" + "<" + adress_uri + "/> .\n"
    gff_prefix_buffer += pr + "\t" + rdf_ns + "<" + rdf + "> .\n"
    gff_prefix_buffer += pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n"
    gff_prefix_buffer += pr + "\t" + xsd_ns + "<" + xsd + "> .\n"
    gff_prefix_buffer += pr + "\t" + owl_ns + "<" + owl + "> .\n"
    gff_prefix_buffer += pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n"
    gff_prefix_buffer += pr + "\t" + obo_ns + "<" + obo_uri + "> .\n"
    gff_prefix_buffer += pr + "\t" + chromosome_ns + "<" + chromosome_uri + "> .\n"
    gff_prefix_buffer += pr + "\t" + res_ns + "<" + resource + "> .\n"
    for listOfuri in listOfURI:
        gff_prefix_buffer += pr + "\t" + listOfuri + ":" + "<" + adress_uri + "/" + listOfuri + "/> .\n"
    line_pre_adder(path_output_file, gff_prefix_buffer)
    print(gff_prefix_buffer)
    rdf_writer.closed
    interval = time.time() - start_time
    print 'Total time in seconds:', interval


def line_pre_adder(filename, line_to_prepend):
    """
    The function add the prefixes in head of the file
    :param filename: name of file where the rdf data will to be writing
    :param line_to_prepend: data that to be wrinte
    :return: nothing
    """
    f = fileinput.input(filename, inplace=1)
    for xline in f:
        if f.isfirstline():
            print line_to_prepend.rstrip('\r\n') + '\n' + xline,
        else:
            print xline,



gff_model2rdf('/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/urgi/pseudomolecul_wheat.gff', '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/urgi_ttl/pseudomolecul_wheat.ttl', "Oryza sativa Indica Group", "http://www.southgreen.fr/agrold/whead_graph", '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/urgi/alias_file')


'''
if __name__ == "__main__":
    gff_parser()
'''