import sys
print (sys.path)
from globalVars import *
import pprint
import re
import os


#Read and save header of file------------------------------------------------------------------------
def SaveHeader(tropGene_file):

    #map_reader = open(tropGene_file)
    header = tropGene_file.readline()
    print("The header: "+ header +"\n\n")
    header = re.sub('"', '', header)
    header = re.sub(';;', ' ', header)
    headerListe = header.split()
    print(type(headerListe))
    print(headerListe)
    return headerListe


#Parser----------------------------------------------------------------------------------------------
def tropGeneParser(topGene_file):

    map_reader = open(topGene_file, "r")
    print("---------> " + str(type(map_reader)))
    counter = 0;
    headers = list()
    headers = SaveHeader(map_reader)
    map_ds = list()
    print("*********************************lecture du fichier ********************************\n")
    lines = map_reader.readlines()
    for line in lines:
        counter+= 1
        line = re.sub('\n$', '', line)
        print(line)
        line = re.sub('"', '', line)

        records = line.split(';;') #('\t')
        map_ds.append(dict(zip(headers, records)))
    map_reader.close()
    print ("TropGene QTL has been parsed!\n")
    print("*************************************\n\n")
    CounterCasted = str(counter)
    print("number of data parsed "+ CounterCasted +"\n\n")
    return map_ds
#---------------------------------------------------------------------------------------------------------

# test pour le parsing generique
#pp = pprint.PrettyPrinter(indent=4)
path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/tropGene/rice.csv'
path_output = '/media/elhassouni/donnees/Noeud-plante-projet/code-source/tropGene-Sorti-Test-pasing-generique.ttl'
#ds = SaveHeader(path)
tropGeneParser(path)

#pp.pprint(ds)
