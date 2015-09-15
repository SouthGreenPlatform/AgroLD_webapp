
import urllib
import pprint
import re
import os
from globalVars import *
from globalVars import base_vocab_ns
from gffParser import *

__author__  = "el hassouni"



def os_indicaModele(indica_ds, output_file):

  # The differentes variable declaration

    tropGene_buffer = ''    # initilised the buffer at zero
    number_match_part, number_match_part_cdna, number_match_part_cds, number_match_part_exon = 0, 0, 0, 0
    rdf_writer = open(output_file, "w")
    chromosome_list = list()
    line_number = 0

# The first wrinting in the file is the prefix

    print ("*************TropGene RDF conversion begins***********\n")
    rdf_writer.write(base + "\t" + "<" + base_uri + "> .\n")
    rdf_writer.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    rdf_writer.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    rdf_writer.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    rdf_writer.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    rdf_writer.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    rdf_writer.write(pr + "\t" + marker_ns + "<" + marker_uri + "> .\n")
    rdf_writer.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    rdf_writer.write(pr + "\t" + cDNA_ns + "<" + cDNA_uri + "> .\n")
    rdf_writer.write(pr + "\t" + ensembl_ns + "<" + ensembl_plant + "> .\n")
    rdf_writer.write(pr + "\t" + mRNA_ns + "<" + mRNA_uri + "> .\n")
    rdf_writer.write(pr + "\t" + protein_ns + "<" + protein_uri + "> .\n")
    rdf_writer.write(pr + "\t" + OrygenesDB_ns + "<" + OrygenesDB_uri + "> .\n")
    rdf_writer.write(pr + "\t" + chromosome_ns + "<" + chromosome_uri + "> .\n")

 # In here we buil the modele and writer in file with ttl format

    for records in indica_ds:
        line_number+=1
        if not records['seqid'] in chromosome_list:

            tropGene_buffer = ''
            chromosome_list.append(records['seqid'])
            tropGene_buffer += chromosome_ns + re.sub('Osi', '', records['seqid']) + "\n"
            tropGene_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39946" + " ;\n"
            tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000430" + " .\n"
            print(tropGene_buffer)
            rdf_writer.write(tropGene_buffer)

        if records['source'] == "Marker":
            tropGene_buffer = ''
            tropGene_buffer += marker_ns + records['attributes']['ID'] + "\n"
            tropGene_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
            tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000343" + " ;\n"
            tropGene_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39946" + " ;\n"
            tropGene_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
            tropGene_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
            tropGene_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + chromosome_ns + re.sub('Osi', '', records['seqid']) + " .\n"
            print(tropGene_buffer)
            rdf_writer.write(tropGene_buffer)


        if records['source'] == "OA_ABa":
            if records['type'] == "match":
                tropGene_buffer = ''
                tropGene_buffer += OrygenesDB_ns + records['attributes']['ID'] + "\n"
                tropGene_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000343" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon__" + "39946" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Osi', '', records['seqid']) + " .\n"
                print(tropGene_buffer)
                rdf_writer.write(tropGene_buffer)

            if records['type'] == "match_part":
                tropGene_buffer = ''
                number_match_part += 1
                tropGene_buffer += OrygenesDB_ns + records['attributes']['Parent'] + "_match_part_" + str(number_match_part) + "\n"
                tropGene_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000039" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39946" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "part_of" + "\t" + OrygenesDB_ns + records['attributes']['Parent'] + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Osi', '', records['seqid']) + " .\n"
                print(tropGene_buffer)
                rdf_writer.write(tropGene_buffer)


        if records['source'] == "cDNA_indica":
            if records['type'] == "EST_match":
                tropGene_buffer = ''
                tropGene_buffer += cDNA_ns + records['attributes']['ID'] + "\n"
                tropGene_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000668" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39946" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + chromosome_ns + re.sub('Osi', '', records['seqid']) + " .\n"
                print(tropGene_buffer)
                rdf_writer.write(tropGene_buffer)


            if records['type'] == "match_part":
                tropGene_buffer = ''
                number_match_part_cdna += 1
                tropGene_buffer += cDNA_ns + records['attributes']['Parent'] + "_match_part_" + str(number_match_part_cdna) + "\n"
                tropGene_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000039" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39946" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "part_of" + "\t" + cDNA_ns + records['attributes']['Parent'] + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Osi', '', records['seqid']) + " .\n"
                print(tropGene_buffer)
                rdf_writer.write(tropGene_buffer)


        if records['source'] == "glean":
            if records['type'] == "gene":
                tropGene_buffer = ''
                tropGene_buffer += ensembl_ns + records['attributes']['ID'] + "\n"
                tropGene_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000704" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39946" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub('Osi', '', records['seqid']) + " .\n"
                print(tropGene_buffer)
                rdf_writer.write(tropGene_buffer)


            if records['type'] == "mRNA":
                tropGene_buffer = ''
                tropGene_buffer += mRNA_ns + records['attributes']['ID'] + "\n"
                tropGene_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000234" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39946" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "develops_from" + "\t\t" + ensembl_ns + records['attributes']['Parent'] + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub('Osi', '', records['seqid']) + " .\n"
                print(tropGene_buffer)
                rdf_writer.write(tropGene_buffer)

            if records['type'] == "polypeptide":
                tropGene_buffer = ''
                tropGene_buffer += protein_ns + records['attributes']['ID'] + "\n"
                tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000104" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39946" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "derives_from" + "\t\t" + mRNA_ns + records['attributes']['Derives_from'] + " ; \n"
                tropGene_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub('Osi', '', records['seqid']) + " .\n"
                print(tropGene_buffer)
                rdf_writer.write(tropGene_buffer)

            if records['type'] == "CDS":
                tropGene_buffer = ''
                number_match_part_cds += 1
                tropGene_buffer += OrygenesDB_ns + records['attributes']['Parent'] + "_CDS_" + str(number_match_part_cds) + "\n"
                tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000316" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39946" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + mRNA_ns + records['attributes']['Parent'] + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub('Osi', '', records['seqid']) + " .\n"
                print(tropGene_buffer)
                rdf_writer.write(tropGene_buffer)

            if records['type'] == "exon":
                tropGene_buffer = ''
                number_match_part_exon += 1
                tropGene_buffer += OrygenesDB_ns + records['attributes']['Parent'] + "_exon_" + str(number_match_part_exon) + "\n"
                tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000147" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39946" + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + mRNA_ns + records['attributes']['Parent'] + " ;\n"
                tropGene_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Osi', '', records['seqid']) + " .\n"
                print(tropGene_buffer)
                rdf_writer.write(tropGene_buffer)
    print(line_number)

    #rdf_writer.write(tropGene_buffer)

'''
in_file = '/opt/TOS_DI-20141207_1530-V5.6.1/workspace/gff_data_orygeneDB/OrygeneDB.gff3'
re = (parseGFF3(in_file))

for mot in re:
    print(mot)
'''
pp = pprint.PrettyPrinter(indent=4)

#path = '/opt/TOS_DI-20141207_1530-V5.6.1/workspace/gff_data_orygeneDB/File_test/indicaCantTest.gff3'
#path_output = '/opt/TOS_DI-20141207_1530-V5.6.1/workspace/gff_data_orygeneDB/File_test/indica_test15.ttl' # The output

path = '/opt/TOS_DI-20141207_1530-V5.6.1/workspace/gff_data_orygeneDB/os_indica_gff3/indicaCant.gff3'     # The input
path_output = '/home/elhassouni/Bureau/indica.ttl' # The output

ds = parseGFF3(path)   # The parsing file withe tropGeneParser()
pp.pprint(ds)    # For to see in teminal the parsing

os_indicaModele(ds, path_output)  # The tranformation fonction tropGeneToRdf(input, output)