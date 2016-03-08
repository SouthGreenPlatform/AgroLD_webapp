import pprint
import re
import os
from globalVars import *
from gffParser import *
from globalVars import base_vocab_ns

__author__  = "el hassouni"


def a_thalianaModeleRDF(indica_ds, output_file):

  # The differentes variable declaration

    a_thaliana_buffer = ''    # initilised the buffer at zero
    number_match_part_sbgi = 0
    number_match_part_kome = 0
    number_exon = 0
    number_cds = 0
    line_number = 0
    number_five_prime_UTR = 0
    number_three_prime_UTR = 0
    rdf_writer = open(output_file, "w")
    chromosome_list = list()

# The first wrinting in the file is the prefix

    print ("*************TropGene RDF conversion begins***********\n")
    rdf_writer.write(base + "\t" + "<" + base_uri + "> .\n")
    rdf_writer.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    rdf_writer.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    rdf_writer.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    rdf_writer.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    rdf_writer.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    rdf_writer.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    rdf_writer.write(pr + "\t" + flanking_ns + "<" + flanking_uri + "> .\n")
    rdf_writer.write(pr + "\t" + ensembl_ns + "<" + ensembl_plant + "> .\n")
    rdf_writer.write(pr + "\t" + mRNA_ns + "<" + mRNA_uri + "> .\n")
    rdf_writer.write(pr + "\t" + OrygenesDB_ns + "<" + OrygenesDB_uri + "> .\n")
    rdf_writer.write(pr + "\t" + chromosome_ns + "<" + chromosome_uri + "> .\n")
    rdf_writer.write(pr + "\t" + embl_ns + "<" + embl_uri + "> .\n")
    rdf_writer.write(pr + "\t" + protein_ns + "<" + protein_uri + "> .\n")

 # In here we buil the modele and writer in file with ttl format

    for records in indica_ds:
        line_number+=1
        # Chromosome triple
        if not records['seqid'] in chromosome_list:
            a_thaliana_buffer = ''
            chromosome_list.append(records['seqid'])
            a_thaliana_buffer += chromosome_ns + re.sub('At', '', records['seqid']) + "\n"
            a_thaliana_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "3702" + " ;\n"
            a_thaliana_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            a_thaliana_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000430" + " .\n"
            print(a_thaliana_buffer)
            rdf_writer.write(a_thaliana_buffer)

# CSH.gff3 for flanking_region and flanking_region type


        if records['type'] == "flanking_region":
            a_thaliana_buffer = ''
            a_thaliana_buffer += embl_ns + records['attributes']['ID'] + "\n"
            a_thaliana_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
            a_thaliana_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
            a_thaliana_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            a_thaliana_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000239" + " ;\n"
            a_thaliana_buffer += "\t" + base_vocab_ns + "has_evalue" + "\t\t" + " \""+ records['attributes']['evalue'] + "\"" + "; \n"
            a_thaliana_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "3702" + " ;\n"
            a_thaliana_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
            a_thaliana_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
            a_thaliana_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('At', '', records['seqid']) + " .\n"
            print(a_thaliana_buffer)
            rdf_writer.write(a_thaliana_buffer)

#RAPDB.gff3

        if records['source'] == "TAIR10":
            if records['type'] == "gene":
                a_thaliana_buffer = ''
                a_thaliana_buffer += ensembl_ns + records['attributes']['ID'] + "\n"
                a_thaliana_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                a_thaliana_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                a_thaliana_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000704" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "3702" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub('At', '', records['seqid']) + " .\n"
                print(a_thaliana_buffer)
                rdf_writer.write(a_thaliana_buffer)


            if records['type'] == "mRNA":
                a_thaliana_buffer = ''
                a_thaliana_buffer += mRNA_ns + records['attributes']['ID'] + "\n"
                a_thaliana_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                a_thaliana_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                a_thaliana_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000234" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "3702" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "develops_from" + "\t\t" + ensembl_ns + records['attributes']['Parent'] + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub('At', '', records['seqid']) + " .\n"
                print(a_thaliana_buffer)
                rdf_writer.write(a_thaliana_buffer)

            if records['type'] == "polypeptide":
                a_thaliana_buffer = ''
                a_thaliana_buffer += protein_ns + records['attributes']['ID'] + "\n"
                a_thaliana_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                a_thaliana_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                a_thaliana_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000104" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "3702" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "derives_from" + "\t\t" + mRNA_ns + records['attributes']['Derives_from'] + " ; \n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub('At', '', records['seqid']) + " .\n"
                print(a_thaliana_buffer)
                rdf_writer.write(a_thaliana_buffer)

            if records['type'] == "CDS":
                a_thaliana_buffer = ''
                number_cds += 1
                id_cds = re.split(',', records['attributes']['Parent'])
                a_thaliana_buffer += OrygenesDB_ns + id_cds[1] + "_CDS_" + str(number_cds) + "\n"
                a_thaliana_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                a_thaliana_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000316" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "3702" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + mRNA_ns + records['attributes']['Parent'] + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub('At', '', records['seqid']) + " .\n"
                print(a_thaliana_buffer)
                rdf_writer.write(a_thaliana_buffer)

            if records['type'] == "exon":
                a_thaliana_buffer = ''
                number_exon += 1
                a_thaliana_buffer += OrygenesDB_ns + records['attributes']['Parent'] + "_EXON_" + str(number_exon) + "\n"
                a_thaliana_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                a_thaliana_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000147" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "3702" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + mRNA_ns + records['attributes']['Parent'] + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('At', '', records['seqid']) + " .\n"
                print(a_thaliana_buffer)
                rdf_writer.write(a_thaliana_buffer)

            if records['type'] == "three_prime_UTR":
                a_thaliana_buffer = ''
                number_three_prime_UTR += 1
                a_thaliana_buffer += OrygenesDB_ns + records['attributes']['Parent'] + "_three_prime_UTR_" + str(number_three_prime_UTR) + "\n"
                a_thaliana_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                a_thaliana_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000205" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "3702" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + mRNA_ns + records['attributes']['Parent'] + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('At', '', records['seqid']) + " .\n"
                print(a_thaliana_buffer)
                rdf_writer.write(a_thaliana_buffer)

            if records['type'] == "five_prime_UTR":
                a_thaliana_buffer = ''
                number_five_prime_UTR += 1
                a_thaliana_buffer += OrygenesDB_ns + records['attributes']['Parent'] + "_five_prime_UTR_" + str(number_five_prime_UTR) + "\n"
                a_thaliana_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                a_thaliana_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000204" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "3702" + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:integer ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + mRNA_ns + records['attributes']['Parent'] + " ;\n"
                a_thaliana_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('At', '',records['seqid']) + " .\n"
                print(a_thaliana_buffer)
                rdf_writer.write(a_thaliana_buffer)

    print(line_number)


'''
pp = pprint.PrettyPrinter(indent=4)

path = '/opt/TOS_DI-20141207_1530-V5.6.1/workspace/gff_data_orygeneDB/a.thaliana/a.thalianaCat.gff3'    # The input
path_output = '/home/elhassouni/Bureau/thaliana.ttl' # The output

#path = '/opt/TOS_DI-20141207_1530-V5.6.1/workspace/gff_data_orygeneDB/File_test/thaliana_test.gff3'    # The input
#path_output = '/opt/TOS_DI-20141207_1530-V5.6.1/workspace/gff_data_orygeneDB/File_test/thaliana_test.ttl' # The output

ds = parseGFF3(path)   # The parsing file withe tropGeneParser()
pp.pprint(ds)    # For to see in teminal the parsing

a_thalianaModele(ds, path_output)  # The path_output)  # The tranformation fonction tropGeneToRdf(input, output)


'''