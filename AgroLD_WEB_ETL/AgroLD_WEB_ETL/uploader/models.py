from __future__ import unicode_literals
from collections import namedtuple
import urllib
import pprint


import gzip
import urllib
import pprint


from django.db import models
import os

# Create your models here.




class Document(models.Model):
    docfile = models.FileField(upload_to='documents/%Y/%m/%d')





class transform(object):

    def lecteur(self, fichier):
        for line in fichier.docfile:
            print(line)
        return "lecture fini"




class gffparser(object):

    def __init__(self):

    #Initialized GeneInfo named tuple. Note: namedtuple is immutable
        self.gffInfoFields = ["seqid", "source", "type", "start", "end", "score", "strand", "phase", "attributes"]
        self.GFFRecord = namedtuple("GFFRecord", self.gffInfoFields)

    def parseGFFAttributes(self, attributeString):
        """Parse the GFF3 attribute column and return a dict"""
        if attributeString == ".": return {}
        ret = {}
        for attribute in attributeString.split(";"):
            key, value = attribute.split("=")
            ret[urllib.unquote(key)] = urllib.unquote(value)
        return ret

    def parseGFF3(self, filename):
        """
        A minimalistic GFF3 format parser.
        Yields objects that contain info about a single GFF3 feature.

        Supports transparent gzip decompression.
        """
        #Parse with transparent decompression
        map_ds = list()
        #openFunc = open
        for line in filename:
            if line.startswith("#"): continue
            parts = line.strip().split("\t")
            #If this fails, the file format is not standard-compatible
            #assert len(parts) == len(self.gffInfoFields)
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
                "attributes": self.parseGFFAttributes(parts[8])
            }
            map_ds.append(normalizedInfo)
            print(normalizedInfo)
            print("\n")
            #Alternatively, you can emit the dictionary here, if you need mutability:
            #    yield normalizedInfo
            #yield GFFRecord(**normalizedInfo)
        return map_ds



class gffToRDF(object):




def os_indicaModeleRDF(indica_ds, output_file):

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