import pprint
import re
import os
from globalVars import base_vocab_ns
from TropgeneParser import *
__author__ = 'elhassouni'



def tropGeneToRDF(tropGene_map, output_file):
# The differentes variable declaration
    tropGene_buffer = ''    # initilised the buffer at zero
    population_counter, mapefeature_counter, study_counter, qtl_counter, trait_counter = 0, 0, 0, 0, 0
    rdf_writer = open(output_file, "w")
    study_list = list()
    qtl_list = list()
    population_list = list()
# The first wrinting in the file is the prefix
    print ("*************TropGene RDF conversion begins***********\n")
    rdf_writer.write(base + "\t" + "<" + base_uri + "> .\n")
    rdf_writer.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    rdf_writer.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    rdf_writer.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    rdf_writer.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    rdf_writer.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    rdf_writer.write(pr + "\t" + study_ns + "<" + study_uri + "> .\n")
    rdf_writer.write(pr + "\t" + population_ns + "<" + population_uri + "> .\n")
    rdf_writer.write(pr + "\t" + qtl_ns + "<" + qtl_uri + "> .\n")
    rdf_writer.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    rdf_writer.write(pr + "\t" + edam_ns + "<" + edam_uri + "> .\n")
    rdf_writer.write(pr + "\t" + trait_ns + "<" + trait_uri + "> .\n")
    rdf_writer.write(pr + "\t" + mapfeature_ns + "<" + mapfeature_uri + "> .\n")
    #Ajout du prefix pour la realese des donnees
    rdf_writer.write(pr + "\t" + res_ns + "<" + resource + "> .\n\n")

 # Study writings: here we browsing files for write the study resources
    for records in tropGene_map:
        if not records['study_id'] in study_list:
            study_counter += 1
            study_list.append(records['study_id'])
            #print(study_list)
            study_id_now = records['study_id']
            tropGene_buffer += study_ns + records['study_id'] + "\n"
            tropGene_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['study_name'] + "\" ;\n"
            tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Study" + " ;\n"
            #tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            #tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + "OBI_0000073" + " ;\n"
            for records in tropGene_map:
                if records['study_id'] == study_id_now:
                    tropGene_buffer += "\t" + base_vocab_ns + "has_observation" + "\t" + qtl_ns + records['qtl_id'] + " ;\n"
            for records in tropGene_map:
                if records['study_id'] == study_id_now and not records['population_id'] in population_list:
                    population_list.append(records['population_id'])
                    tropGene_buffer += "\t" + base_vocab_ns + "has_population" + "\t" + population_ns + records['population_id'] + " ;\n"
            tropGene_buffer = re.sub(' ;$', ' .', tropGene_buffer)
        rdf_writer.write(tropGene_buffer)
        tropGene_buffer = ''    # reset the buffer at zero
        population_list = list()
    print(tropGene_buffer)
 # Population wrinting: here we browsing files for write the study resources
    tropGene_buffer = ''    # reset the buffer at zero
    for records in tropGene_map:

        if not records['population_id'] in population_list:
            if records['study_id'] in study_list:
                population_list.append(records['population_id'])
                population_counter +=  1
                tropGene_buffer += population_ns + records['population_id'] + " \n"
                tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Population" + " ;\n"
                #tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                #tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + "OBI_0000181" + " ;\n"
                tropGene_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['population_name'] + "\" .\n"
    rdf_writer.write(tropGene_buffer)
    print(tropGene_buffer)
 # QTL  writing: here we browsing files for write the QTL resources
 # writing the Qtl who seo by in the study resource
    tropGene_buffer = ''    # reset the buffer at zero
    for records in tropGene_map:
        if records['study_id'] in study_list:
            qtl_counter += 1
            qtl_id_now = records['qtl_id']
            tropGene_buffer += qtl_ns + records['qtl_id'] + "\n"
            tropGene_buffer += "\t" + rdfs_ns + "label" + "\t" + " \""+ records['qtl_name'] + "\" ;\n"
            tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "QTL" + " ;\n"
            #tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + "SO_0000771" + " ;\n"       # a definir correctement
            for records in tropGene_map:
                if records['qtl_id'] == qtl_id_now:
                    if records['trait_ontology_id'] == 'NULL' or 'na':
                        tropGene_buffer += "\t" + base_vocab_ns + "has_trait" + "\t" + trait_ns + records['trait_id_of_tropgene'] + " ;\n"
                    else:
                        tropGene_buffer += "\t" + base_vocab_ns + "has_trait" + "\t" + trait_ns + records['trait_ontology_id'] + " ;\n"
                    tropGene_buffer += "\t" + base_vocab_ns + "has_mapfeature" + "\t" + mapfeature_ns + records['mapfeature_id'] + " .\n"
    rdf_writer.write(tropGene_buffer)
    print(tropGene_buffer)
# Mapfeature writing: here we browsing files for write the Mapfeature resources
# writing the Mapfeature who seo by in the Qtl resource
    tropGene_buffer = ''    # reset the buffer at zero
    for records in tropGene_map:
        if not records['mapfeature_id'] in tropGene_buffer and records['study_id'] in study_list:
            mapefeature_counter += 1
            tropGene_buffer += mapfeature_ns + records['mapfeature_id'] + "\n"
            tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Map_feature" + " ;\n"
            #tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            #tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + edam_ns + "data_1865" + " ;\n"
            tropGene_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t" + " \"" + records['chromosome'] + "\" ;\n"
            tropGene_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + records['start_position'] + "\"^^xsd:integer ;\n"
            tropGene_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + " \"" + records['stop_position'] + "\"^^xsd:integer .\n"
    rdf_writer.write(tropGene_buffer)
    print(tropGene_buffer)
# Trait writing: here we browsing files for write the Trait resources
# writing the trait who are seo by in the Qtl resource
    tropGene_buffer = ''    # reset the buffer at zero
    for records in tropGene_map:
        if not records['trait_ontology_id'] or not records['trait_id_of_tropgene'] in tropGene_buffer:
            if records['study_id'] in study_list:
                trait_counter += 1
                if records['trait_ontology_id'] == 'NULL' or 'na':
                    tropGene_buffer += trait_ns + records['trait_id_of_tropgene'] + "\n"
                else:
                    tropGene_buffer += trait_ns + records['trait_ontology_id'] + "\n"
            tropGene_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + records['trait_name'] + "\" ;\n"
            tropGene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            if records['trait_description'] != 'NULL':
                tropGene_buffer += "\t" + base_vocab_ns + "description" + "\t" + " \"" + records['trait_description'] + "\" ;\n"
            tropGene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + "TO_0000387" + " .\n"

    rdf_writer.write(tropGene_buffer)
    print(tropGene_buffer)
    rdf_writer.close()


    print("************* TropGene RDF conversion finish ***********\n")
    # Some control for to see the  QTL and Study number
    QTL = "Number of QTL: :" + str(qtl_counter)
    print(QTL)
    STUDY = "Number of STUDY :" + str(study_counter)
    print(STUDY)
    MAPFEATURE = "Number of mapfeature :" + str(mapefeature_counter)
    print(MAPFEATURE)
    TRAIT = "Number of trait :" + str(trait_counter)
    print(TRAIT)
    POPULATION = "Number of population :" + str(population_counter)
    print(POPULATION)

'''
# ---------------------------------------------------------------------------------------------------------
# The test to parsing and transform to RDF data
# ---------------------------------------------------------------------------------------------------------
pp = pprint.PrettyPrinter(indent=4)
#
path = '/media/elhassouni/donnees/Noeud-plante-projet/code-source/test_files/tropgene/rice.csv'     # The input
path_output = '/home/elhassouni/Bureau/Tropgene.ttl' # The output
ds = tropGeneParser(path)   # The parsing file withe tropGeneParser()
pp.pprint(ds)    # For to see in teminal the parsing

tropGeneToRDF(ds, path_output)  # The tranformation fonction tropGeneToRdf(input, output)

# ---------------------------------------------------------------------------------------------------------'' \
'''
