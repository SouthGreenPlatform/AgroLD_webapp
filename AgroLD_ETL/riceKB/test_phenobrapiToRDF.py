# -*- coding: utf-8 -*-
import pprint

import re
import os
from globalVars import *
from globalVars import base_vocab_ns
import json
import io
import urllib2
import requests
from pprint import pprint
import urllib2
import uuid

__author__ = 'elhassouni'

'''
def downloadJson(url_service):

    path_output = '/home/elhassouni/Bureau/pheno_URGI.json'
    rdf_writer = open(path_output, "w")
    page = 0
    pageSize = 1000

    import json
    import urllib2
    pprint(json.load(urllib2.urlopen(url_service)))
    rdf_writer.write(str(json.load(urllib2.urlopen(url_service))))

    while True:
        url = url_service + '?pageSize=' + str(pageSize) + '&page=' + str(page)
        response = requests.get(url)
        content = json.loads(response.content.decode("utf-8"))
        for object in content['result']['data']:
            obj2 = dict(object)
            pprint(obj2)
            rdf_writer.write(str(obj2))
        page += 1
        if page > content['metadata']['pagination']['totalPages'] - 1:
            break

'''

def getdata(url_service):

        path_output = '/home/elhassouni/Bureau/test_URGI.json'
        rdf_writer = open(path_output, "w")
        page = 0
        pageSize = 1000
        while True:
            url = url_service + '?pageSize=' + str(pageSize) + '&page=' + str(page)
            response = requests.get(url)
            #print(response.content)
            #print(url)
            content = json.loads(response.content.decode("utf-8"))
            #print(content)
            for object in content['result']['data']:
                obj2 = dict(object)

                pprint(obj2)

            page +=  1
      #TEST
            if page > 10:
                break



'''

        #Observation buil the dictionary for create the resource ObservationUnitLvelLabel
            level_type = list ()
            level_label = list ()
            if obj4['observationUnitLevels'] != None:
                for data_level_label in obj4['observationUnitLevels']:
                    level_label.append(data_level_label['label'])
                for data_level_type in obj4['observationUnitLevels']:
                    level_type.append(data_level_type['type'])
            observation_dic = []
            observation_dic.append(dict(zip(level_label, level_type)))
            for observation_data in observation_dic:
                for key, value in observation_data.items():
                    phenotype_buffer = ''
                    phenotype_buffer += observationUnitLevelLabel_ns + str(key) + "\n"
                    phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_level_types" + "\t" + " \"" + value + "\" .\n"
                    #print phenotype_buffer
                    #rdf_writer.write(phenotype_buffer.encode('utf8'))




        #ObservationUnitDbId
            phenotype_buffer = ''
            phenotype_buffer += observationUnitDbId_ns + str(obj4["observationUnitDbId"]) + "\n"
            phenotype_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            phenotype_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "bco_0000044" + " ;\n"
        #Probleme au niveau de ces deux clefs doublon et clefs souvent vide issu ouverte pour correction bug
            #phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_y" + "\t" + " \"" + obj4['observationUnitX']  + "\"^^xsd:integer ;\n"
            #phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_x" + "\t" + " \"" + obj4['observationUnitY']  + "\"^^xsd:integer ;\n"
    #        for data_data in obj4['data']:
    #            phenotype_buffer += "\t" + base_vocab_ns + "has_data" + "\t" + " \"" + observationVariableDbId_ns + str(data_data['observationVariableDbId']) + "\" ;\n"

    #        for observation_data in observation_dic:
    #            for key, value in observation_data.items():
    #                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_level_types" + "\t" + " \"" + observationUnitLevelLabel_ns + key + "\" ;\n"
    #        phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_x_label" + "\t" + " \"" + obj4['observationUnitXLabel'] + "\" ;\n"
    #        phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_y_label" + "\t" + " \"" + obj4['observationUnitYLabel'] + "\" .\n"

            #print(phenotype_buffer)
            #rdf_writer.write(phenotype_buffer.encode('utf8'))
        rdf_writer.close()








getdata('https://urgi.versailles.inra.fr/ws/webresources/brapi/v1/phenotypes')





#path = '/home/elhassouni/Bureau/JSON.json'    # The input
#path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/plant_breeding_ttl/pheno_wheat.ttl' # The output
#----------------------------------------------------------------
# lien vers le fichier de données a transformé validé par l'urgi
#----------------------------------------------------------------
#path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/plant_breeding_db/wheat_db/phenotypes_wheat_urgi.json'



#downloadJson('https://urgi.versailles.inra.fr/ws/webresources/brapi/v1/phenotypes')









SAUVEGARDE



# -*- coding: utf-8 -*-
import pprint
import re
from globalVars import *
from globalVars import base_vocab_ns
import json
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
            #print(response.content)
            #print(url)
            content = json.loads(response.content.decode("utf-8"))
            #print(content)
            for object in content['result']['data']:
                obj2 = dict(object)
                #pprint(obj2)
                yield obj2
            page +=  1
      #TEST
            if page > 10:
                break



#def phenoToRDF(file,  output_file):
def pheno2RDF(output_file):
    # TO DO : probleme de redandance au niveau de certaine data
    rdf_writer = open(output_file, "w")
    ########################################################################
    path = '/home/elhassouni/Bureau/JSON.json'    # The input
    obj3 = json.loads(open(path).read())
    ########################################################################
    #######################################################################################
    #En mode test version final
    #data_list = list()
    #obj3 = getdata('https://urgi.versailles.inra.fr/ws/webresources/brapi/v1/phenotypes')
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




#Initialisation des
    data_data_list = list()
    observationUnitDbId_ns_list = list()

    for obj4 in obj3['data']: #supprimer ['data'] pour la version final
    #import des varibles global dans environement contextuelle de la fonction
        global var_studyDbId
        global buffer_study
        global buffer_treatment
        print('valeur de study: ' + str(obj4['studyDbId']))
        print('valeur de varstudy: ' + str(var_studyDbId))


        if obj4['studyDbId'] != var_studyDbId:



            print(observationUnitDbId_ns_list)

            print('Printer' + str(buffer_study))

    #Ecriture des données dans les fichiers
            rdf_writer.write(buffer_study.encode('utf8'))
            rdf_writer.write(buffer_treatment.encode('utf8'))
    #treatment
            treatment_id = int(uuid.uuid4())
            buffer_treatment += treatment_ns + str(treatment_id) + "\n"
            buffer_treatment += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            buffer_treatment  += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "EO_0007359" + " ;\n"
            for data_treatment in obj4['treatments']:
                buffer_treatment += "\t" + base_vocab_ns + "has_factor" + "\t" + " \"" + str(data_treatment['factor']) + "\" ;\n"
                buffer_treatment += "\t" + base_vocab_ns + "has_modality" + "\t" + " \"" + str(data_treatment['modality']) + "\" .\n"
            rdf_writer.write(buffer_treatment.encode('utf8'))
            #print(buffer_treatment)



     #study
            print ('####################################################### Buffer et variables remis a zero ###################################################################################')
            buffer_study += studyDbId_ns + str(obj4["studyDbId"]) + "\n"
            buffer_study += "\t" + rdfs_ns + "label" + "\t" + " \"" + obj4['studyLocation'] + "\" ;\n"
            buffer_study += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            buffer_study += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "OBI_0000073" + " ;\n"
            for unit in observationUnitDbId_ns_list:
                buffer_study += "\t" + base_vocab_ns + "is_about" + "\t" + observationUnitDbId_ns + str(unit) + " ;\n"
            buffer_study += "\t" + base_vocab_ns + "has_treatment" + "\t""\t" + treatment_ns + str(treatment_id) + " ;\n"
            buffer_study += "\t" + base_vocab_ns + "part_of" + "\t" + " \"" + str(obj4['studyProject']) + "\" .\n"
            rdf_writer.write(buffer_study.encode('utf8'))
            print(buffer_study)
    #reinitialisation des variable
            var_studyDbId = obj4['studyDbId']
            buffer_study = ''
            buffer_treatment = ''
            data_data_list = list()
            observationUnitDbId_ns_list = list()






     #data
        for data_data in obj4['data']: #supprimer ['data'] pour la version final
            if data_data['observationVariableId'] not in data_data_list:
                observationUnit_datas = int(uuid.uuid4())
                phenotype_buffer = ''
                phenotype_buffer += observationVariableDbId_ns + str(observationUnit_datas) + "\n"
                data_data_list.append(observationUnit_datas)
                phenotype_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                phenotype_buffer += "\t" + base_vocab_ns + "has_season" + "\t" + " \"" + str(data_data['season']) + "\" ;\n"
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_value" + "\t" + " \"" + str(data_data['observationValue']) + "\" ;\n"
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_time_stamp" + "\t" + " \"" + str(data_data['observationTimeStamp']) + "\" ;\n"
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_variable_id" + "\t" + " \"" + observationVariableDbId_ns + str(re.sub(':', '_',data_data['observationVariableId'])) + "\" .\n"
                rdf_writer.write(phenotype_buffer.encode('utf8'))
            #print phenotype_buffer

    #ObservationUnitDbId
        phenotype_buffer = ''
        phenotype_buffer += observationUnitDbId_ns + str(obj4["observationUnitDbId"]) + "\n"
        observationUnitDbId_ns_list.append(obj4["observationUnitDbId"])
        phenotype_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
        phenotype_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "bco_0000044" + " ;\n"
        phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_x_label" + "\t" + " \"" + obj4['xlabel'] + "\" ;\n"
        phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_y_label" + "\t" + " \"" + obj4['ylabel'] + "\" ;\n"
        phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_x" + "\t" + " \"" + str(obj4['x']) + "\" ;\n"
        phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_y" + "\t" + " \"" + str(obj4['y']) + "\" ;\n"
        for la_data in data_data_list:
            phenotype_buffer += "\t" + base_vocab_ns + "has_datas" + "\t""\t" + observationVariableDbId_ns+str(la_data) + "\" ;\n"
        for observationUnitLevels in obj4['observationUnitLevels']:
            phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_type" + "\t" + " \"" + observationUnitLevels['type'] + "\" ;\n"
            phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_label" + "\t" + " \"" + observationUnitLevels['label'] + "\" .\n"
        rdf_writer.write(phenotype_buffer.encode('utf8'))
        #print(phenotype_buffer)



#path = '/home/elhassouni/Bureau/JSON.json'    # The input
#path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/plant_breeding_ttl/pheno_wheat.ttl' # The output
#----------------------------------------------------------------
# lien vers le fichier de données a transformé validé par l'urgi
#----------------------------------------------------------------
#path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/plant_breeding_db/wheat_db/phenotypes_wheat_urgi.json'
path_output = '/home/elhassouni/Bureau/test_URGI.ttl'

pheno2RDF(path_output)

#downloadJson('https://urgi.versailles.inra.fr/ws/webresources/brapi/v1/phenotypes')














# -*- coding: utf-8 -*-
import pprint
import re
from globalVars import *
from globalVars import base_vocab_ns
import json
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
            #print(response.content)
            #print(url)
            content = json.loads(response.content.decode("utf-8"))
            #print(content)
            for object in content['result']['data']:
                obj2 = dict(object)
                #pprint(obj2)
                yield obj2
            page +=  1
      #TEST
            if page > 60:
                print("finir car page superieur à 60")
                #yield
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




#Initialisation des
    data_data_list = list()
    observationUnitDbId_ns_list = list()

    #for obj4 in obj3['data']: #supprimer ['data'] pour la version final
    for obj4 in obj3:
    #import des varibles global dans environement contextuelle de la fonction
        global var_studyDbId
        global buffer_study
        global buffer_treatment
        #print(obj4['studyDbId'])

        try:
            if obj4['studyDbId'] != var_studyDbId:
                #print(obj4['studyDbId'])
                old_var_studyDbId = var_studyDbId
                var_studyDbId = obj4['studyDbId']
                print('sauvaegarde de ID --> ' + str(var_studyDbId))

                if observationUnitDbId_ns_list == []:
                    print(observationUnitDbId_ns_list)
                    for factoryModality in obj4['treatments']:
                        factory = factoryModality['factor']
                        modality = factoryModality['modality']
                    studyProjet = obj4['studyProject']
                    studyLocation = obj4['studyLocation']

                if obj4['studyDbId'] == var_studyDbId:
                    print(observationUnitDbId_ns_list)
                    print(obj4['treatments'])
                    if obj4['observationUnitLevels'] == None:
                        factory = "None"
                        modality = "None"
                    else:
                        print('--------------------------------------> '+ str(obj4['treatments']))
                        #for factoryModality in obj4['treatments']:
                        #   factory = factoryModality['factor']
                        #  modality = factoryModality['modality']
                    studyProjet = obj4['studyProject']
                    studyLocation = obj4['studyLocation']

                    print('Location --> ' + studyLocation)
                   # print('projet --> ' +studyProjet)
                    print('factory--> ' +factory)
                    print('modality --> ' + modality)
                    print('old var Study --> ' + str(old_var_studyDbId))


                if observationUnitDbId_ns_list != []:
                    print('test si pas vide ' + str(observationUnitDbId_ns_list))
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
                    print(buffer_treatment)
             #study
                    print ('####################################################### Buffer et variables remis a zero ###################################################################################')
                    print(studyLocation)
                    buffer_study += studyDbId_ns + str(old_var_studyDbId) + "\n"
                    buffer_study += "\t" + rdfs_ns + "label" + "\t" + " \"" + studyLocation + "\" ;\n"
                    buffer_study += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                    buffer_study += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "OBI_0000073" + " ;\n"
                    for unit in observationUnitDbId_ns_list:
                        buffer_study += "\t" + base_vocab_ns + "is_about" + "\t" + observationUnitDbId_ns + str(unit) + " ;\n"
                    buffer_study += "\t" + base_vocab_ns + "has_treatment" + "\t""\t" + treatment_ns + str(treatment_id) + " ;\n"
                    buffer_study += "\t" + base_vocab_ns + "part_of" + "\t" + " \"" + str(studyProjet) + "\" .\n"
                    rdf_writer.write(buffer_study.encode('utf8'))
                    print(buffer_study)
            #reinitialisation des variable

                    buffer_study = ''
                    buffer_treatment = ''
                    data_data_list = list()
                    observationUnitDbId_ns_list = list()
                    print('test doit etre vide --> ' + str(observationUnitDbId_ns_list))

        except:
            old_var_studyDbId = var_studyDbId
            var_studyDbId = "None"



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
            if obj4['xlabel'] == None:
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_x_label" + "\t" + " \"" + "None" + "\" ;\n"
            else:
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_x_label" + "\t" + " \"" + obj4['xlabel'] + "\" ;\n"
            if obj4['xlabel'] == None:
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_y_label" + "\t" + " \"" + "None" + "\" ;\n"
            else:
                phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_y_label" + "\t" + " \"" + obj4['ylabel'] + "\" ;\n"

            phenotype_data = ''
            #data
            #for data_data in obj4['data']: #supprimer ['data'] pour la version final
            print(obj4)
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
            else:
                print('--------------------------------------> '+  str(obj4['observationUnitLevels']))
                #for observationUnitLevels in obj4['observationUnitLevels']:
                #    phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_type" + "\t" + " \"" + observationUnitLevels['type'] + "\" ;\n"
                #    phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_label" + "\t" + " \"" + observationUnitLevels['label'] + "\" .\n"
            phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_x" + "\t" + " \"" + str(obj4['x']) + "\" ;\n"
            phenotype_buffer += "\t" + base_vocab_ns + "has_observation_unit_y" + "\t" + " \"" + str(obj4['y']) + "\" .\n"
            rdf_writer.write(phenotype_buffer.encode('utf8'))
            data_data_list = list()
            #print(phenotype_buffer)

#path = '/home/elhassouni/Bureau/JSON.json'    # The input
#path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/plant_breeding_ttl/pheno_wheat.ttl' # The output
#----------------------------------------------------------------
# lien vers le fichier de données a transformé validé par l'urgi
#----------------------------------------------------------------
#path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/plant_breeding_db/wheat_db/phenotypes_wheat_urgi.json'
path_output = '/home/elhassouni/Bureau/test_URGI.ttl'

pheno2RDF(path_output)

#downloadJson('https://urgi.versailles.inra.fr/ws/webresources/brapi/v1/phenotypes')



