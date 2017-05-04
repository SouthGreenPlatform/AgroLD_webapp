# -*- coding: utf-8 -*-
import pprint
import re
from globalVars import *
from globalVars import base_vocab_ns
import json
import json as simplejson
import requests
import uuid


__author__ = 'elhassouni'

#Varible globals
global buffer_study
global buffer_treatment
global var_studyDbId
var_studyDbId = ''
buffer_study = ''
buffer_treatment = ''


def getdata(url_service):
        page = 0
        pageSize = 1000
        while True:
            url = url_service + '?pageSize=' + str(pageSize) + '&page=' + str(page)
            response = requests.get(url)
            try:
                print(page)
                content = json.loads(response.content.decode("utf-8"))
                requests.get(url).json()
                for object in content['result']['data']:
                    obj2 = dict(object)
                    yield obj2
                page += 1
            except:
                print("Error about data format sended by URGI Phenotype")
                break
            if page > 152:
                print("Translate to RDF END")
                break



#def phenoToRDF(file,  output_file):
def pheno2RDF(output_file):
    # TO DO : probleme de redandance au niveau de certaine data
    rdf_writer = open(output_file, "w")
    #MODE TEST desactivé
    ########################################################################
    #path = '/home/elhassouni/Bureau/JSON.json'    # The input
    #obj3 = json.loads(open(path).read())
    ########################################################################
    #######################################################################################
    #En mode test version final
    data_list = list()
    obj3 = getdata('https://urgi.versailles.inra.fr/ws/webresources/brapi/v1/phenotypes')
    #######################################################################################
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

    factory = ''
    modality = ''
    studyProjet = ''
    studyLocation = ''

#Initialisation des Variables
    data_data_list = list()
    observationUnitDbId_ns_list = list()

    #for obj4 in obj3['data']: #supprimer ['data'] pour la version final
    for obj4 in obj3:
    #import des varibles global dans environement contextuelle de la fonction
        global var_studyDbId
        global buffer_study
        global buffer_treatment
        #print(obj4['studyDbId'])

        if obj4['studyDbId'] != var_studyDbId:
            #print('study ====== >' + str(obj4['studyDbId']))
            old_var_studyDbId = var_studyDbId
            var_studyDbId = obj4['studyDbId']
            #print('sauvaegarde de ID --> ' + str(var_studyDbId))

            if observationUnitDbId_ns_list == []:
                #print(observationUnitDbId_ns_list)
                for factoryModality in obj4['treatments']:
                    factory = factoryModality['factor']
                    modality = factoryModality['modality']
                studyProjet = obj4['studyProject']
                studyLocation = obj4['studyLocation']

            if obj4['studyDbId'] == var_studyDbId:
                #print(observationUnitDbId_ns_list)
                #print(obj4['treatments'])
                if obj4['observationUnitLevels'] == None:
                    factory = "None"
                    modality = "None"
                #else pour le test
                # else:
                    #print('--------------------------------------> '+ str(obj4['treatments']))
                    #for factoryModality in obj4['treatments']:
                    #   factory = factoryModality['factor']
                    #  modality = factoryModality['modality']
                studyProjet = obj4['studyProject']
                studyLocation = obj4['studyLocation']

                #print('Location --> ' + studyLocation)
                #print('projet --> ' +studyProjet)
                #print('factory--> ' +factory)
                #print('modality --> ' + modality)
                #print('old var Study --> ' + str(old_var_studyDbId))

            if observationUnitDbId_ns_list != []:
                #print('test si pas vide ' + str(observationUnitDbId_ns_list))
                #rdf_writer.write(buffer_study.encode('utf8'))
                #rdf_writer.write(buffer_treatment.encode('utf8'))

        #Ecriture des données dans les fichiers
        #treatment
                treatment_id = int(uuid.uuid4())
                buffer_treatment += treatment_ns + str(treatment_id) + "\n"
                buffer_treatment += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                buffer_treatment  += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "EO_0007359" + " ;\n"
                buffer_treatment += "\t" + base_vocab_ns + "has_factor" + "\t" + " \"" + str(factory) + "\" ;\n"
                buffer_treatment += "\t" + base_vocab_ns + "has_modality" + "\t" + " \"" + str(modality) + "\" .\n"
                rdf_writer.write(buffer_treatment.encode('utf8'))
                #print(buffer_treatment)
         #study
                #print ('####################################################### Buffer et variables remis a zero ###################################################################################')
                #print(studyLocation)
                buffer_study += studyDbId_ns + str(old_var_studyDbId) + "\n"
                buffer_study += "\t" + rdfs_ns + "label" + "\t" + " \"" + studyLocation + "\" ;\n"
                buffer_study += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                buffer_study += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "OBI_0000073" + " ;\n"
                for unit in observationUnitDbId_ns_list:
                    buffer_study += "\t" + base_vocab_ns + "is_about" + "\t" + observationUnitDbId_ns + str(unit) + " ;\n"
                buffer_study += "\t" + base_vocab_ns + "has_treatment" + "\t""\t" + treatment_ns + str(treatment_id) + " ;\n"
                buffer_study += "\t" + base_vocab_ns + "part_of" + "\t" + " \"" + str(studyProjet) + "\" .\n"
                rdf_writer.write(buffer_study.encode('utf8'))
                #print(buffer_study)
        #reinitialisation des variable

                buffer_study = ''
                buffer_treatment = ''
                data_data_list = list()
                observationUnitDbId_ns_list = list()
                #print('test doit etre vide --> ' + str(observationUnitDbId_ns_list))

            #print('observationVariableDbId_ns --> ' + observationVariableDbId_ns)
            #ObservationUnitDbId
            #if data_data_list != []:
            phenotype_buffer = ''
            if obj4["observationUnitDbId"] == None:
                phenotype_buffer += observationUnitDbId_ns + "None" + "\n"
            else:
                phenotype_buffer += observationUnitDbId_ns + str(obj4["observationUnitDbId"]) + "\n"
            if obj4["observationUnitDbId"] not in observationUnitDbId_ns_list:
                observationUnitDbId_ns_list.append(obj4["observationUnitDbId"])
            phenotype_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            phenotype_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "bco_0000044" + " ;\n"
            if obj4['XLabel'] == None:
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_x_label" + "\t" + " \"" + "None" + "\" ;\n"
            else:
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_x_label" + "\t" + " \"" + obj4['XLabel'] + "\" ;\n"
            if obj4['YLabel'] == None:
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_y_label" + "\t" + " \"" + "None" + "\" ;\n"
            else:
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_y_label" + "\t" + " \"" + obj4['YLabel'] + "\" ;\n"

            phenotype_data = ''
            #data
            #for data_data in obj4['data']: #supprimer ['data'] pour la version final
            #print(obj4)
            for data_data in obj4['data']:
                if data_data['observationVariableId'] not in data_data_list:
                    observationUnit_datas = int(uuid.uuid4())
                    phenotype_data = ''
                    phenotype_data += observationVariableDbId_ns + str(observationUnit_datas) + "\n"
                    data_data_list.append(observationUnit_datas)
                    phenotype_data += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                    phenotype_data += "\t" + base_vocab_ns + "has_season" + "\t" + " \"" + str(data_data['season']) + "\" ;\n"
                    phenotype_data += "\t" + base_vocab_ns + "has_observation_value" + "\t" + " \"" + str(data_data['observationValue']) + "\" ;\n"
                    phenotype_data += "\t" + base_vocab_ns + "has_observation_time_stamp" + "\t" + " \"" + str(data_data['observationTimeStamp']) + "\" ;\n"
                    phenotype_data += "\t" + base_vocab_ns + "has_observation_variable_id" + "\t" + " \"" + str(re.sub(':', '_',data_data['observationVariableId'])) + "\" .\n"
                    rdf_writer.write(phenotype_data.encode('utf8'))
                #print phenotype_buffer
            for la_data in data_data_list:
                phenotype_buffer += "\t" + base_vocab_ns + "has_datas" + "\t""\t" + observationVariableDbId_ns+str(la_data) + " ;\n"
            #Modification du format mise en commentaire car l'assemble des cas trouvé est a none pour observationUnitLevels type et label
            # juste et fonctionnel actuellement
            if obj4['observationUnitLevels'] == None:
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_type" + "\t" + " \"" + "null" + "\" ;\n"
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_label" + "\t" + " \"" + "null" + "\" ;\n"
            #Else pour le test
            # else:
             #   print('--------------------------------------> '+  str(obj4['observationUnitLevels']))
                #for observationUnitLevels in obj4['observationUnitLevels']:
                #    phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_type" + "\t" + " \"" + observationUnitLevels['type'] + "\" ;\n"
                #    phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_label" + "\t" + " \"" + observationUnitLevels['label'] + "\" .\n"
            phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_x" + "\t" + " \"" + str(obj4['X']) + "\" ;\n"
            phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_y" + "\t" + " \"" + str(obj4['Y']) + "\" .\n"
            rdf_writer.write(phenotype_buffer.encode('utf8'))
            data_data_list = list()
            print(phenotype_buffer)

#path = '/home/elhassouni/Bureau/JSON.json'    # The input
#path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/plant_breeding_ttl/pheno_wheat.ttl' # The output
#----------------------------------------------------------------
# lien vers le fichier de données a transformé validé par l'urgi
#----------------------------------------------------------------
#path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/plant_breeding_db/wheat_db/phenotypes_wheat_urgi.json'
path_output = '/home/elhassouni/Bureau/test_URGI2.ttl'

pheno2RDF(path_output)

#downloadJson('https://urgi.versailles.inra.fr/ws/webresources/brapi/v1/phenotypes')



