import pprint
import re
import os
from globalVars import *
from globalVars import base_vocab_ns
import json
import io


from pprint import pprint

__author__ = 'elhassouni'



def phenoToRDF(file,  output_file):

    json_data=open(file)
    rdf_writer = open(output_file, "w")
    data = json.load(json_data)


#variables
    treatment = 0
    treatment_list = list()
    data_list = list()



#print ("*************RDF conversion begins***********\n")
    rdf_writer.write(base + "\t" + "<" + base_uri + "> .\n")
    rdf_writer.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    rdf_writer.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    rdf_writer.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    rdf_writer.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    rdf_writer.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    rdf_writer.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    rdf_writer.write(pr + "\t" + observationUnitDbId_ns + "<" + observationUnitDbId_uri + "> .\n")
    rdf_writer.write(pr + "\t" + studyDbId_ns + "<" + studyDbId_uri + "> .\n")
    rdf_writer.write(pr + "\t" + treatment_ns + "<" + treatment_uri + "> .\n")
    rdf_writer.write(pr + "\t" + observationUnitLevelLabel_ns + "<" + observationUnitLevelLabel_uri + "> .\n")
    rdf_writer.write(pr + "\t" + observationVariableDbId_ns + "<" + observationVariableDbId_uri + "> .\n\n")
    


#Study
    phenotype_buffer = ''
    phenotype_buffer += studyDbId_ns + str(data["studyDbId"]) + "\n"
    phenotype_buffer += "\t" + rdfs_ns + "label" + "\t" + " \"" + data['studyLocation'] + "\" ;\n"
    phenotype_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
    phenotype_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "OBI_0000073" + " ;\n"
    phenotype_buffer += "\t" + base_vocab_ns + "part_of" + "\t" + " \"" + data['studyProject'] + "\" ;\n"
    phenotype_buffer += "\t" + base_vocab_ns + "has_treatment" + "\t" + " \"" + treatment_ns + str(treatment) + "\" .\n"

    print(phenotype_buffer)
    rdf_writer.write(phenotype_buffer.encode('utf8'))

#treatement
    phenotype_buffer = ''

    phenotype_buffer += treatment_ns + str(treatment) + "\n"
    phenotype_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
    phenotype_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "EO_0007359" + " ;\n"
    for data_treatment  in data['treatments']:
        treatment_list.append(treatment)
        phenotype_buffer += "\t" + base_vocab_ns + "has_modality" + "\t" + " \"" + data_treatment['modality'] + "\" ;\n"
        phenotype_buffer += "\t" + base_vocab_ns + "has_factor" + "\t" + " \"" + data_treatment['factor'] + "\" .\n"
        treatment = treatment + 1

    print(phenotype_buffer)
    rdf_writer.write(phenotype_buffer.encode('utf8'))

#data
    for data_data in data['data']:
        phenotype_buffer = ''
        phenotype_buffer += observationVariableDbId_ns + str(data_data['observationVariableDbId']) + "\n"
        phenotype_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
        phenotype_buffer += "\t" + base_vocab_ns + "has_season" + "\t" + " \"" + data_data['season'] + "\" ;\n"
        phenotype_buffer += "\t" + base_vocab_ns + "has_observation_value" + "\t" + " \"" + data_data['observationValue'] + "\" ;\n"
        phenotype_buffer += "\t" + base_vocab_ns + "has_observation_time_stamp" + "\t" + " \"" + data_data['observationTimeStamp'] + "\" ;\n"

        # to define because observationVariableId it possible to been ID or PUI (DOI, URI, LSID)
        #phenotype_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "EO_0007359" + " ;\n"
        phenotype_buffer += "\t" + base_vocab_ns + "has_observation_variable_id" + "\t" + " \"" + data_data['observationVariableId'] + "\" .\n"

        print(phenotype_buffer)
        rdf_writer.write(phenotype_buffer.encode('utf8'))

#Observation buil the dictionary for create the resource ObservationUnitLvelLabel
    level_type = list ()
    level_label = list ()
    for data_level_label in data['observationUnitLevelLabels']:
        level_label.append(data_level_label)
    for data_level_type in data['observationUnitLevelTypes']:
        level_type.append(data_level_type)
    observation_dic = []
    observation_dic.append(dict(zip(level_label, level_type)))
    for observation_data in observation_dic:
        for key, value in observation_data.items():
            phenotype_buffer = ''
            phenotype_buffer += observationUnitLevelLabel_ns + str(key) + "\n"
            phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_level_types" + "\t" + " \"" + value + "\" .\n"
            print phenotype_buffer
            rdf_writer.write(phenotype_buffer.encode('utf8'))


#ObservationUnitDbId
    phenotype_buffer = ''
    phenotype_buffer += observationUnitDbId_ns + str(data["observationUnitDbId"]) + "\n"
    phenotype_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
    phenotype_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "bco_0000044" + " ;\n"
    phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_y" + "\t" + " \"" + data['observationUnitX']  + "\"^^xsd:integer ;\n"
    phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_x" + "\t" + " \"" + data['observationUnitY']  + "\"^^xsd:integer ;\n"
    for data_data in data['data']:
        phenotype_buffer += "\t" + base_vocab_ns + "has_data" + "\t" + " \"" + observationVariableDbId_ns + str(data_data['observationVariableDbId']) + "\" ;\n"

    for observation_data in observation_dic:
        for key, value in observation_data.items():
            phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_level_types" + "\t" + " \"" + observationUnitLevelLabel_ns + key + "\" ;\n"
    phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_x_label" + "\t" + " \"" + data['observationUnitXLabel'] + "\" ;\n"
    phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_y_label" + "\t" + " \"" + data['observationUnitYLabel'] + "\" .\n"

    print(phenotype_buffer)
    rdf_writer.write(phenotype_buffer.encode('utf8'))



    json_data.close()
    rdf_writer.close()



path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/plant_breeding_db/wheat_db/observationUnit-2758890.json'    # The input

#path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/plant_breeding_ttl/pheno_wheat.ttl' # The output

#----------------------------------------------------------------
# lien vers le fichier de données a transformé validé par l'urgi
#----------------------------------------------------------------
#path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/plant_breeding_db/wheat_db/phenotypes_wheat_urgi.json'
path_output = '/home/elhassouni/Bureau/test_URGI.ttl'
phenoToRDF(path, path_output)

