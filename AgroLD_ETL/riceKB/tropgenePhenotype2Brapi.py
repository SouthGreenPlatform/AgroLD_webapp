__author__ = 'elhassouni'

import sys
print (sys.path)
from globalVars import *
import pprint
import re
import os



def SaveHeader(tropGene_file):

    #tropGene_file = open(tropGene_file)
    header = tropGene_file.readline()
    #print("The header: "+ header +"\n\n")
    header = re.sub('"', '', header)
    header = re.sub(';', ' ', header)
    headerListe = header.split()
    print(headerListe)
    return headerListe



def tropgenePhenotype(topGene_file):

    map_reader = open(topGene_file, "r")
    counter = 0;
    headers = list()
    headers = SaveHeader(map_reader)
    map_ds = list()
    print("*********************************lecture du fichier ********************************\n")
    lines = map_reader.readlines()
    for line in lines:
        counter+= 1
        line = re.sub('\n$', '', line)
        #print(line)
        line = re.sub('"', '', line)

        records = line.split(';;') #('\t')
        map_ds.append(dict(zip(headers, records)))
    map_reader.close()
    print ("TropGene QTL has been parsed!\n")
    print("*************************************\n\n")
    CounterCasted = str(counter)
    print("number of data parsed "+ CounterCasted +"\n\n")
    return map_ds


def tropgene2brapi(map_ds):

    brapi_buffer = ''
    observationUnitDbId = 0

    brapi_buffer_entete = '{ \n ' \
                          '"metadata": { \n ' \
                          '    "pagination": { \n ' \
                          '      "pageSize": "Null", \n ' \
                          '      "currentPage": "Null", \n ' \
                          '      "totalCount": "Null", \n ' \
                          '      "totalPages": "Null" \n ' \
                          '    }, \n ' \
                          '    "status": [] \n ' \
                          ' }, \n ' \
                          '  "result": { \n ' \
                          '    "data": [ \n ' \
                          '      { \n '
    for data in map_ds:
        print data['study_type']

        brapi_buffer += '{"observationUnitDbId": 2880513,  \n ' \
                        '"observationUnitPUI": null, \n ' \
                        '"studyId": "BTH_Dijon_2014_SetA1", \n ' \
                        '"studyDbId": 781, \n ' \
                        '"studyLocation": "' + data['name'] + ' ", \n ' \
                        '"studyPUI": null, \n' \
                        '"studyProject": "Null", \n ' \
                        '"studySets": [ \n ' \
                        '"INRA Wheat Network BRC accession (A series)" \n ' \
                        ' "INRA Small Grain Cereals Network" \n ' \
                        '], \n ' \
                        '"studyPlatform": null, \n ' \
                        '"observationUnitLevels": [ \n ' \
                        '{ \n ' \
                        '"type": "Null", \n ' \
                        '"label": "Null" \n ' \
                        '} \n ' \
                        '], \n ' \
                        '"germplasmPUI": null, \n ' \
                        '"germplasmDbId": Null, \n ' \
                        '"germplasmName": "Null", \n ' \
                        '"treatments": [ \n ' \
                        '{ \n ' \
                        '"factor": "Null", \n ' \
                        '"modality": "Null" \n ' \
                        '} \n ' \
                        '], \n ' \
                        '"attributes": null, \n ' \
                        '"data": [ \n ' \
                        '{ \n ' \
                        '"instanceNumber": null, \n ' \
                        ' "observationVariableId": "Null", \n ' \
                        ' "season": "' + data['year'] + '", \n ' \
                        '"observationValue": "Null", \n ' \
                        '"observationTimeStamp": null, \n ' \
                        '"quality": null, \n ' \
                        '"collectionFacilityLabel": null, \n ' \
                        ' "collector": null \n ' \
                        ' } \n ' \
                        '], \n ' \
                        '"xlabel": "Null", \n ' \
                        '"ylabel": "Null", \n ' \
                        '"y": null, \n ' \
                        '"x": null, \n ' \
                        'XLabel": "Null", \n ' \
                        '"X": null, \n ' \
                        '"YLabel": "Null", \n ' \
                        '"Y": null \n ' \
                        '},'

        # TO DO PARCOURIR ET FORMATER LE LE FICHIER TROPGENE






#test pour le parsing generique
pp = pprint.PrettyPrinter(indent=4)
path = '/media/elhassouni/donnees/Noeud-plante-projet/tropgene_model_data_doc/study(2).csv'

path_output = '/media/elhassouni/donnees/Noeud-plante-projet/tropgene_model_data/tropgenePhenotypeBrapi.json'
#ds = SaveHeader(path)
#pp.pprint(ds)

ds = tropgenePhenotype(path)
pp.pprint(ds)
tropgene2brapi(ds)