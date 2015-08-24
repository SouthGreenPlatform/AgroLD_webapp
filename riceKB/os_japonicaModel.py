import pprint
import re
import os
from riceKB.globalVars import *
from riceKB.globalVars import base_vocab_ns
from riceKB.gffParser import *

__author__  = "el hassouni"


def os_indicaModele(indica_ds, output_file):

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
    otl_plante = list()

# The first wrinting in the file is the prefix

    print ("*************TropGene RDF conversion begins***********\n")
    rdf_writer.write(base + "\t" + "<" + base_uri + "> .\n")
    rdf_writer.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    rdf_writer.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    rdf_writer.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    rdf_writer.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    rdf_writer.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    rdf_writer.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    rdf_writer.write(pr + "\t" + mirbase_ns + "<" + mirbase_uri + "> .\n")
    rdf_writer.write(pr + "\t" + mirbase_mature_ns + "<" + mirbase_mature_uri + "> .\n")
    rdf_writer.write(pr + "\t" + protein_ns + "<" + protein_uri + "> .\n")
    rdf_writer.write(pr + "\t" + flanking_ns + "<" + flanking_uri + "> .\n")
    rdf_writer.write(pr + "\t" + ensembl_ns + "<" + ensembl_plant + "> .\n")
    rdf_writer.write(pr + "\t" + mRNA_ns + "<" + mRNA_uri + "> .\n")
    rdf_writer.write(pr + "\t" + cDNA_ns + "<" + cDNA_uri + "> .\n")
    rdf_writer.write(pr + "\t" + otl_public_plante_ns + "<" + otl_public_plante_uri + "> .\n")
    rdf_writer.write(pr + "\t" + OrygenesDB_ns + "<" + OrygenesDB_uri + "> .\n")
    rdf_writer.write(pr + "\t" + chromosome_ns + "<" + chromosome_uri + "> .\n")
    rdf_writer.write(pr + "\t" + embl_ns + "<" + embl_uri + "> .\n")

 # In here we buil the modele and writer in file with ttl format

    for records in indica_ds:
        line_number+=1
        # Chromosome triple
        if not records['seqid'] in chromosome_list:
            os_japonica_buffer = ''
            chromosome_list.append(records['seqid'])
            os_japonica_buffer += chromosome_ns + re.sub('Os', '', records['seqid']) + "\n"
            os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
            os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000430" + " .\n"
            print(os_japonica_buffer)
            rdf_writer.write(os_japonica_buffer)

# SGBI.gff3 for EST_match and match_part type

        if records['source'] == "SBGI":
            if records['type'] == "EST_match":
                os_japonica_buffer = ''
                os_japonica_buffer += OrygenesDB_ns + records['attributes']['ID'] + "\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000668" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)


            if records['type'] == "match_part":
                os_japonica_buffer = ''
                number_match_part_sbgi += 1
                os_japonica_buffer += OrygenesDB_ns + records['attributes']['Parent'] + "_match_part_" + str(number_match_part_sbgi) + "\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000039" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "part_of" + "\t" + OrygenesDB_ns + records['attributes']['Parent'] + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)


# SSR.gff3 for match type #

        if records['source'] == "Rice_SSR":
            if records['type'] == "match":
                os_japonica_buffer = ''
                os_japonica_buffer += OrygenesDB_ns + records['attributes']['ID'] + "\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000343" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)


# OTL_public.gff3 for flanking_region type #

        if records['source'] == "OTL_T-DNA":
            if records['type'] == "flanking_region":
                os_japonica_buffer = ''
                os_japonica_buffer += embl_ns + records['attributes']['ID'] + "\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000239" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_evalue" + "\t\t" + " \""+ records['attributes']['evalue'] + "\"" + "; \n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + otl_public_plante_ns + records['attributes']['plant_name'] + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)

 # Plante_name of OTL_file.gff3

            if not records['attributes']['plant_name'] in otl_plante:
                os_japonica_buffer = ''
                otl_plante.append(records['attributes']['plant_name'])
                os_japonica_buffer += otl_public_plante_ns + records['attributes']['plant_name'] + "\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)




# CSIRO.gff3 for flanking_region and flanking_region type

        if records['source'] == "CSIRO":
            if records['type'] == "flanking_region":
                os_japonica_buffer = ''
                os_japonica_buffer += embl_ns + records['attributes']['ID'] + "\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000239" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_evalue" + "\t\t" + " \""+ records['attributes']['evalue'] + "\"" + "; \n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)


# miRNA.gff3 for flanking_region and mirRNA and miRNA_primaru-transcrypion type

        if records['source'] == "miRBase":
            if records['type'] == "miRNA_primary_transcript":
                os_japonica_buffer = ''
                os_japonica_buffer += mirbase_ns + records['attributes']['ID'] + "\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000647" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)

            if records['type'] == "miRNA":
                os_japonica_buffer = ''
                os_japonica_buffer += mirbase_mature_ns + records['attributes']['ID'] + "\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000276" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "derives_from" + "\t\t" + mirbase_ns + records['attributes']['Derives_from'] + " ; \n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)



# KOME.gff3
        if records['source'] == "KOME":
            if records['type'] == "cDNA_match":
                os_japonica_buffer = ''
                os_japonica_buffer += cDNA_ns + records['attributes']['ID'] + "\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000689" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)

            if records['type'] == "match_part":
                os_japonica_buffer = ''
                number_match_part_kome += 1
                os_japonica_buffer += OrygenesDB_ns + records['attributes']['Parent'] + "_match_part_" + str(number_match_part_kome) + "\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000039" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + cDNA_ns + records['attributes']['Parent'] + " ; \n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)


#RAPDB.gff3

        if records['source'] == "IRGSP":
            if records['type'] == "gene":
                os_japonica_buffer = ''
                os_japonica_buffer += ensembl_ns + records['attributes']['ID'] + "\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "source_project" + "\t" + " \"" + records['source'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000704" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)


            if records['type'] == "mRNA":
                os_japonica_buffer = ''
                os_japonica_buffer += mRNA_ns + records['attributes']['ID'] + "\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000234" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "develops_from" + "\t\t" + ensembl_ns + records['attributes']['Parent'] + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)

            if records['type'] == "polypeptide":
                os_japonica_buffer = ''
                os_japonica_buffer += protein_ns + records['attributes']['ID'] + "\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['attributes']['Name'] + "\" ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000104" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "derives_from" + "\t\t" + mRNA_ns + records['attributes']['Derives_from'] + " ; \n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)

            if records['type'] == "CDS":
                os_japonica_buffer = ''
                number_cds += 1
                os_japonica_buffer += OrygenesDB_ns + records['attributes']['Parent'] + "_CDS_" + str(number_cds) + "\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000316" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + mRNA_ns + records['attributes']['Parent'] + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + "" + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)

            if records['type'] == "exon":
                os_japonica_buffer = ''
                number_exon += 1
                os_japonica_buffer += OrygenesDB_ns + records['attributes']['Parent'] + "_EXON_" + str(number_exon) + "\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000147" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + mRNA_ns + records['attributes']['Parent'] + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)

            if records['type'] == "three_prime_UTR":
                os_japonica_buffer = ''
                number_three_prime_UTR += 1
                os_japonica_buffer += OrygenesDB_ns + records['attributes']['Parent'] + "_three_prime_UTR_" + str(number_three_prime_UTR) + "\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000205" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + mRNA_ns + records['attributes']['Parent'] + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)

            if records['type'] == "five_prime_UTR":
                os_japonica_buffer = ''
                number_five_prime_UTR += 1
                os_japonica_buffer += OrygenesDB_ns + records['attributes']['Parent'] + "_five_prime_UTR_" + str(number_five_prime_UTR) + "\n"
                os_japonica_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                os_japonica_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000204" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "taxon" + "\t\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + str(records['start']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + str(records['end']) + "\"^^xsd:int ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "part_of" + "\t\t" + mRNA_ns + records['attributes']['Parent'] + " ;\n"
                os_japonica_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t\t" + " " + chromosome_ns + re.sub('Os', '', records['seqid']) + " .\n"
                print(os_japonica_buffer)
                rdf_writer.write(os_japonica_buffer)

    print(line_number)



pp = pprint.PrettyPrinter(indent=4)


#TEST PARAM
#path = '/opt/TOS_DI-20141207_1530-V5.6.1/workspace/gff_data_orygeneDB/os_japonica/japonica_test.gff3'
#ath_output = '/opt/TOS_DI-20141207_1530-V5.6.1/workspace/gff_data_orygeneDB/os_japonica/japonica_test.ttl' # The output


path = '/opt/TOS_DI-20141207_1530-V5.6.1/workspace/gff_data_orygeneDB/os_japonica/os_indicaCancat.gff3'    # The input
path_output = '/home/elhassouni/Bureau/japonica.ttl' # The output
ds = parseGFF3(path)   # The parsing file withe tropGeneParser()
pp.pprint(ds)    # For to see in teminal the parsing

os_indicaModele(ds, path_output)  # The path_output)  # The tranformation fonction tropGeneToRdf(input, output)


