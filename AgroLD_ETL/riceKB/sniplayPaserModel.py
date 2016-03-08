from __future__ import with_statement
import pprint
from globalVars import *
import re
import os
import time


__author__ = 'elhassouni'


# Variable Globals ###################################################################################################

# Variable
path = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/sniplay_db/sniplay_test.txt'
path_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/sniplay_db_ttl/sniplay.ttl' # The output
output = open(path_output, "w")
pp = pprint.PrettyPrinter(indent=4)

# Definition des liste globals
alleles_liste_AA = list()
gene_list = list()
chromosome_list = list()
individu_list=list()
alleleliste = []
listeIndividu = list()
identifiantAlleliste = list ()
#identifiantAlleliste = list ()



# Definition de la liste des clefs
realine = open(path, 'r')
header = realine.readline()
header = re.sub('"', '', header)
header = re.sub(';;', ' ', header)
headerListe = header.split()
#print(headerListe)
dictionnaireAllele = {}
print(headerListe)
headerListe.remove("rs#")
headerListe.remove("alleles")
headerListe.remove("chrom")
headerListe.remove("pos")
headerListe.remove("gene")

print(headerListe)

for item in headerListe:

    dictionnaireAllele[item] = []



# Variblae d'incrementation
number_allele = 1
count = 0
allele_incre = 0

# Variable Globals ###################################################################################################

# Fontion de recherche des items pour charque allele AA, CC, TT, GG pour les individus
cherchecle = lambda d, val: [c for c,v in d.items() if v==val]


def SaveHeader():

    realine = open(path, 'r')
    header = realine.readline()
    header = re.sub('"', '', header)
    header = re.sub(';;', ' ', header)
    headerListe = header.split()
    return headerListe



#Methode qui prend en parametre le fichier
#retourne la liste des donne

def ParseLine(file):
    lineFile= list
    lineFile = file.split('\t')
    map_ds = list()
    headers = list()
    headers = SaveHeader()
    map_ds.append(dict(zip(headers, lineFile)))
    return map_ds


#Methode non necessaire
def buildeDicoVariant(listeIndividu):
    AA = {'AA':[]}
    for listeaa in listeIndividu:
        AA['AA'].append(listeaa)
    return AA


#Methode pour afficher l'ensemeble des donne d'un dictionnaire
#utiliser que si necessaire
def AfficheData(data):
    for key,values in data.items():
        for val in values:
            print(val)



#Methode trouver les individu avec le variant AA avec en entre le fichier.
#appel de la methode ParseIndividu pour recuper a chaque ligne les donnees
def FoundVariantGG(file_in):
    dataParsed = ParseLine(file_in)
    for records in dataParsed:
        listeGG = (cherchecle(records,'GG'))
        #print(listeGG)
    return listeGG


#Methode trouver les individu avec le variant AA avec en entre le fichier.
#appel de la methode ParseIndividu pour recuper a chaque ligne les donnees
def FoundVariantAA(file_in):
    dataParsed = ParseLine(file_in)
    for records in dataParsed:
        listeGG = (cherchecle(records,'AA'))
        #print(listeGG)
    return listeGG


def FoundVariantTT(file_in):
    dataParsed = ParseLine(file_in)
    for records in dataParsed:
        listeTT = (cherchecle(records,'TT'))
        #print(listeGG)
    return listeTT

def FoundVariantCC(file_in):
    dataParsed = ParseLine(file_in)
    for records in dataParsed:
        listeCC = (cherchecle(records,'CC'))
        #print(listeGG)
    return listeCC

def increment():
    global allele_incre
    allele_incre = allele_incre+1


def ParseHapMap():
    map_ds = list()
    map_reader = open(path, "r")
    for line in map_reader:
        if line.startswith("rs#"): continue
        parts = line.strip().split("\t")
        normalizedInfo = {
            "rs#": None if parts[0] == "." else parts[0],
            "alleles": None if parts[1] == "." else parts[1],
            "chrom": None if parts[2] == "." else parts[2],
            "pos": None if parts[3] == "." else parts[3],
            "gene": None if parts[4] == "." else parts[4],
            "feature": None if parts[5] == "." else parts[5],
            "effect": None if parts[6] == "." else parts[6],
            "codon_change": None if parts[7] == "." else parts[7],
            "amino_acid_change": None if parts[8] == "." else parts[8],
            "MAF": None if parts[9] == "." else parts[9],
            "missing_data": None if parts[10] == "." else parts[10],
            "AA": FoundVariantAA(line),
            "TT": FoundVariantTT(line),
            "CC": FoundVariantCC(line),
            "GG": FoundVariantGG(line)

        }
        sniplayModel(normalizedInfo)


def writeStudy():
    sniplay_buffer = ''
    sniplay_buffer += study_ns + "GBS_Courtois_et_al_2013"  "\n"
    for key, value in dictionnaireAllele.iteritems():
        sniplay_buffer += "\t" + base_vocab_ns + "is_about" + "\t" + sniplay_individual_ns + key+ " ;\n"
    sniplay_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
    sniplay_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "OBI_0000073" + " . \n"
    output.write(sniplay_buffer)
    print(sniplay_buffer)

# Write sniplay
def sniplayModel(sniplay_ds):
    sniplay_buffer = ''

    if not sniplay_ds['chrom'] in chromosome_list:
            sniplay_buffer = ''
            chromosome_list.append(sniplay_ds['chrom'])
            chromosome = int(str(re.sub('Chr', '', sniplay_ds['chrom'])))
            if chromosome < 10:
                sniplay_buffer += chromosome_ns + "0" + str(chromosome) + "\n"
            else:sniplay_buffer += chromosome_ns + str(chromosome) + "\n"
            sniplay_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            sniplay_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000430" + " . \n"
            print(sniplay_buffer)
            output.write(sniplay_buffer)

    if not sniplay_ds['gene'] in gene_list:
        if sniplay_ds['gene'] == "intergenic:":
            intergenic = sniplay_ds['chrom'] +"_" + sniplay_ds['pos'] + "_intergenic"
            if not intergenic in gene_list:
                gene_list.append(intergenic)
                sniplay_buffer = ''
                sniplay_buffer += sniplay_gene_integenic_ns + sniplay_ds['chrom'] +"_" + sniplay_ds['pos'] + "_intergenic" + " \n"
                sniplay_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                sniplay_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000704" + " ;\n"
                if sniplay_ds['feature'] != "#":
                    sniplay_buffer += "\t" + base_vocab_ns + "has_feature" + "\t" + sniplay_ds['feature'] + " ;\n"
                sniplay_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + sniplay_ds['pos'] + "\" ;\n"
                sniplay_buffer += "\t" + base_vocab_ns + "has_missing_data" + "\t" + " \"" + str(float(re.sub('%', '',sniplay_ds['missing_data']))/100) + "\"^^xsd:float ;\n"
                if sniplay_ds['amino_acid_change'] != "#":
                    sniplay_buffer += "\t" + base_vocab_ns + "has_amino_acid_change" + "\t" + " \"" + sniplay_ds['amino_acid_change'] + "\" ;\n"
                if sniplay_ds['effect'] != "#":
                    sniplay_buffer += "\t" + base_vocab_ns + "has_effect" + "\t" + " \"" + sniplay_ds['effect'] + "\" ;\n"
                if sniplay_ds['codon_change'] != "#":
                    sniplay_buffer += "\t" + base_vocab_ns + "has_codon_change" + "\t" + " \"" + sniplay_ds['codon_change'] + "\";\n"

                if sniplay_ds['AA']:
                    for indiv in sniplay_ds['AA']:
                        sniplay_buffer += "\t" + base_vocab_ns + "part_of" + "\t""\t" + sniplay_individual_ns + indiv + ";\n"
                        #if not indiv in listeIndividu:
                        #   writeIndividu(indiv,sniplay_ds)

                if sniplay_ds['CC']:
                    for indiv in sniplay_ds['CC']:
                        sniplay_buffer += "\t" + base_vocab_ns + "part_of" + "\t""\t" + sniplay_individual_ns + indiv + ";\n"
                        #if not indiv in listeIndividu:
                        #    writeIndividu(indiv,sniplay_ds)

                if sniplay_ds['TT']:
                    for indiv in sniplay_ds['TT']:
                        sniplay_buffer += "\t" + base_vocab_ns + "part_of" + "\t""\t" + sniplay_individual_ns + indiv + ";\n"
                        #if not indiv in listeIndividu:
                        #    writeIndividu(indiv,sniplay_ds)

                if sniplay_ds['GG']:
                    for indiv in sniplay_ds['GG']:
                        sniplay_buffer += "\t" + base_vocab_ns + "part_of" + "\t""\t" + sniplay_individual_ns + indiv + ";\n"
                        #if not indiv in listeIndividu:
                        #    writeIndividu(indiv,sniplay_ds)



        else:
            sniplay_buffer = ''
            gene_list.append(sniplay_ds['gene'])
            sniplay_buffer += sniplay_gene_ns + re.sub(':', '', sniplay_ds['gene']) + " \n"
            sniplay_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            sniplay_buffer += "\t" + rdfs_ns + "subClassOf" + "\t\t" + obo_ns + "SO_0000704" + " ;\n"
            if sniplay_ds['feature'] != "#":
                sniplay_buffer += "\t" + base_vocab_ns + "has_feature" + "\t" + " \"" +sniplay_ds['feature'] + "\" ;\n"
            sniplay_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + " \"" + sniplay_ds['pos'] + "\" ;\n"
            sniplay_buffer += "\t" + base_vocab_ns + "has_missing_data" + "\t" + " \"" + str(float(re.sub('%', '',sniplay_ds['missing_data']))/100) + "\"^^xsd:float ;\n"
            if sniplay_ds['amino_acid_change'] != "#":
                sniplay_buffer += "\t" + base_vocab_ns + "has_amino_acid_change" + "\t" + " \"" + sniplay_ds['amino_acid_change'] + "\";\n"
            if sniplay_ds['effect'] != "#":
                sniplay_buffer += "\t" + base_vocab_ns + "has_effect" + "\t" + " \"" + sniplay_ds['effect'] + "\" ;\n"
            if sniplay_ds['codon_change'] != "#":
                sniplay_buffer += "\t" + base_vocab_ns + "has_codon_change" + "\t" + " \"" + sniplay_ds['codon_change'] + "\" ;\n"


            if sniplay_ds['AA']:
                    for indiv in sniplay_ds['AA']:
                        sniplay_buffer += "\t" + base_vocab_ns + "part_of" + "\t""\t" + sniplay_individual_ns + indiv + ";\n"


            if sniplay_ds['CC']:
                    for indiv in sniplay_ds['CC']:
                        sniplay_buffer += "\t" + base_vocab_ns + "part_of" + "\t""\t" + sniplay_individual_ns + indiv + ";\n"
                        #if not indiv in listeIndividu:
                        #    writeIndividu(indiv,sniplay_ds)

            if sniplay_ds['TT']:
                    for indiv in sniplay_ds['TT']:
                        sniplay_buffer += "\t" + base_vocab_ns + "part_of" + "\t""\t" + sniplay_individual_ns + indiv + ";\n"
                        #if not indiv in listeIndividu:
                        #   writeIndividu(indiv,sniplay_ds)

            if sniplay_ds['GG']:
                    for indiv in sniplay_ds['GG']:
                        sniplay_buffer += "\t" + base_vocab_ns + "part_of" + "\t""\t" + sniplay_individual_ns + indiv + ";\n"
                        #if not indiv in listeIndividu:
                         #   writeIndividu(indiv,sniplay_ds)

        for indiv in sniplay_ds['AA']:
                sniplay_buffer += "\t" + base_vocab_ns + "has_allele" + "\t""\t" + sniplay_allele_ns + sniplay_ds['pos'] + "_" + indiv + "_AA_" + str(allele_incre) + " ;\n"
                identifiant = sniplay_ds['pos'] + "_" + indiv + "_AA_" + str(allele_incre) + "_MAF_" + sniplay_ds['MAF']
                dictionnaireAllele[indiv].append(str(identifiant))
        for indiv in sniplay_ds['CC']:
                sniplay_buffer += "\t" + base_vocab_ns + "has_allele" + "\t""\t" + sniplay_allele_ns + sniplay_ds['pos'] + "_" + indiv + "_CC_" + str(allele_incre) + " ;\n"
                identifiant = sniplay_ds['pos'] + "_" + indiv + "_CC_" + str(allele_incre) + "_MAF_" + sniplay_ds['MAF']
                dictionnaireAllele[indiv].append(str(identifiant))
        for indiv in sniplay_ds['TT']:
                sniplay_buffer += "\t" + base_vocab_ns + "has_allele" + "\t""\t" + sniplay_allele_ns + sniplay_ds['pos'] + "_" + indiv + "_TT_" + str(allele_incre) + " ;\n"
                identifiant = sniplay_ds['pos'] + "_" + indiv + "_TT_" + str(allele_incre) + "_MAF_" + sniplay_ds['MAF']
                dictionnaireAllele[indiv].append(str(identifiant))
        for indiv in sniplay_ds['GG']:
                sniplay_buffer += "\t" + base_vocab_ns + "has_allele" + "\t""\t" + sniplay_allele_ns + sniplay_ds['pos'] + "_" + indiv + "_GG_" + str(allele_incre) + " ;\n"
                identifiant = sniplay_ds['pos'] + "_" + indiv + "_GG_" + str(allele_incre) + "_MAF_" + sniplay_ds['MAF']
                dictionnaireAllele[indiv].append(str(identifiant))
        chrom = int(str(re.sub('Chr', '', sniplay_ds['chrom'])))
        if chrom < 10:
            sniplay_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t" + chromosome_ns + "0" + str(chrom) + " .\n"
        else:
            sniplay_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t" + chromosome_ns + str(chrom) + " .\n"

        increment()
        print(sniplay_buffer)
        output.write(sniplay_buffer)





def writeIndividu():
    sniplay_buffer = ''
    for key, value in dictionnaireAllele.iteritems():
        sniplay_buffer += sniplay_individual_ns + key + " \n"
        for val in value:
             valSplit = val.split('_MAF_')
             sniplay_buffer += "\t" + base_vocab_ns + "has_allele" + "\t""\t" + sniplay_allele_ns + valSplit[0] + " ;\n"
        sniplay_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
        sniplay_buffer += "\t" + rdfs_ns + "subClassOf" + "\t""\t" + co_ns + "CO_715:0000225" + " .\n"

    print(sniplay_buffer)
    output.write(sniplay_buffer)





def writeAllele():

    sniplay_buffer = ''
    for key, value in dictionnaireAllele.iteritems():
        for val in value:
             valSplit = val.split('_MAF_')
             sniplay_buffer += sniplay_allele_ns  + valSplit[0] + " \n"
             sniplay_buffer += "\t" + base_vocab_ns + "has_maf" + "\t" + " \"" + str(float(re.sub('%', '',valSplit[1]))/100) + "\"^^xsd:float ;\n"
             variant = valSplit[0].split('_')
             if 'AA' in variant:
                 sniplay_buffer += "\t" + base_vocab_ns + "variant" + "\t" + "\"" + 'AA' + "\" ;\n"
             if 'CC' in variant:
                 sniplay_buffer += "\t" + base_vocab_ns + "variant" + "\t" + "\"" + 'CC' + "\"  ;\n"
             if 'TT' in variant:
                 sniplay_buffer += "\t" + base_vocab_ns + "variant" + "\t" + "\"" + 'TT' + "\"  ;\n"
             if 'GG' in variant:
                 sniplay_buffer += "\t" + base_vocab_ns + "variant" + "\t" + "\"" + 'GG' + "\"  ;\n"
             sniplay_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
             sniplay_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + "OBI_0001352" + " . \n"

    print(sniplay_buffer)
    output.write(sniplay_buffer)


# Sauvegarde dans un dictionnaire l'ensembe des donnee

def identifiantAllele():

    map_reader = open(path, "r")
    for line in map_reader:
        if line.startswith("rs#"): continue
        parts = line.strip().split("\t")
        normalizedInfo = {
            "rs#": None if parts[0] == "." else parts[0],
            "alleles": None if parts[1] == "." else parts[1],
            "chrom": None if parts[2] == "." else parts[2],
            "pos": None if parts[3] == "." else parts[3],
            "gene": None if parts[4] == "." else parts[4],
            "MAF": None if parts[9] == "." else parts[9],
            "AA": FoundVariantAA(line),
            "TT": FoundVariantTT(line),
            "CC": FoundVariantCC(line),
            "GG": FoundVariantGG(line)
        }
        alleleliste.append(normalizedInfo)


def removeAlleleListe():
    global alleleliste
    alleleliste = ''


def writePrefix():

    output.write(base + "\t" + "<" + base_uri + "> .\n")
    output.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    output.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    output.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    output.write(pr + "\t" + owl_ns + "<" + owl_uri + "> .\n")
    output.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    output.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    output.write(pr + "\t" + chromosome_ns + "<" + chromosome_uri + "> .\n")
    output.write(pr + "\t" + sniplay_pos_ns + "<" + sniplay_pos_uri + "> .\n")
    output.write(pr + "\t" + sniplay_consequence_ns + "<" + sniplay_consequence_uri + "> .\n")
    output.write(pr + "\t" + study_ns + "<" + study_uri + "> .\n")
    output.write(pr + "\t" + sniplay_gene_ns + "<" + sniplay_gene_uri + "> .\n")
    output.write(pr + "\t" + sniplay_gene_integenic_ns + "<" + sniplay_gene_integenic_uri + "> .\n")
    output.write(pr + "\t" + sniplay_individual_ns + "<" + sniplay_individual_uri + "> .\n")
    output.write(pr + "\t" + sniplay_allele_ns + "<" + sniplay_allele_uri + "> .\n")
    output.write(pr + "\t" + co_ns + "<" + co_uri + "> .\n")


# MAIN #################################################################################################################

def sniplayPaserModel():

    avant = time.clock()
    identifiantAllele()
    #print(alleleliste)
    writePrefix()
    # La Fonction ParserHapMap ecrit les chromosome et les genes
    ParseHapMap()
    writeIndividu()
    writeAllele()
    writeStudy()
    print 'Time execution : ',time.clock() - avant


