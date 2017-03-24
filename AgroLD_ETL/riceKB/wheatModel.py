# -*- coding: utf-8 -*-
import re
from globalVars import *
from globalVars import base_vocab_ns
from gffParser import *
import urllib2
import json
import uuid
import fileinput
import time

__author__  = "el hassouni"

import pprint


def aliaschromosome(topGene_file):
    map_reader = open(topGene_file, "r")
    headers = ['alias', 'chromosome']
    map_ds = list()
    lines = map_reader.readlines()
    for line in lines:
        line = re.sub('\n$', '', line)
        records = line.split('=') #('\t')
        map_ds.append(dict(zip(headers, records)))
    map_reader.close()
    print(map_ds)
    return map_ds



def generateur(parents):
    for g in searchParents(parents, ds):
        return g



def annotation(texte):
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
    annotations = get_json(REST_URL + "/annotator?text=" + urllib2.quote(texte) +"&ontologies=EFO,NCBITAXON&longest_only=true")
    taxon = return_annotations(annotations)
    return taxon


def bleModel(ble_ds, output_file, text_to_annotate, adress_uri, alias_file):
    alias_dico = aliaschromosome(alias_file)
    adress_uri
    taxon_id = annotation(text_to_annotate)
    rdf_writer = open(output_file, "w")
    chromosome_list = list()
    listOfURI = list()

    for records in ble_ds:
        gff_buffer = ''
        if records['seqid'] not in chromosome_list:
            for alias_f in alias_dico:
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
                    id = generateur(records['attributes']['Parent'])
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
    line_pre_adder(output_file, gff_prefix_buffer)
    print(gff_prefix_buffer)
    rdf_writer.closed


def line_pre_adder(filename, line_to_prepend):
    f = fileinput.input(filename, inplace=1)
    for xline in f:
        if f.isfirstline():
            print line_to_prepend.rstrip('\r\n') + '\n' + xline,
        else:
            print xline,


def searchParents(Parent, ds):
    for records in ds:
        try:
            if records['attributes']['ID'] == Parent:
                yield (records['type'])
        except:
            searchParents(Parent, ds)




start_time = time.time()



#MAIN
pp = pprint.PrettyPrinter(indent=4)

path_alias_file = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/urgi/alias_file'

aliaschromosome(path_alias_file)

#path = '/home/elhassouni/Bureau/ta3bPseudomolecule.anno.gff3'

path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/urgi/pseudomolecul_wheat.gff'     # The input

path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/urgi_ttl/ble.ttl' # The output

ds = parseGFF3(path)

bleModel(ds, path_output,"Oryza sativa Indica Group", "http://www.southgreen.fr/agrold/whead_graph", path_alias_file)  # The tranformation fonction tropGeneToRdf(input, output)

interval = time.time() - start_time
print 'Total time in seconds:', interval


#IDPrents = searchParents("TRAES3BF007400010CFD_t1", ds)
#for i in generator:
#print(generator)