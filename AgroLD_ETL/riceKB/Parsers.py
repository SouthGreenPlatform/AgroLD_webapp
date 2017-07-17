'''
Created on Aug 4, 2014

@author: venkatesan
'''

import re
import os
import pprint


class GrameneGenomeParser(object):
    '''
    classdocs
    '''

    def __init__(self, infile):
        '''
        Constructor
        '''
        self.infile = infile
    
    def parse(self):
        geneHash = {}
    
        fileReader = open(self.infile, "r")
        lines = fileReader.readlines()
        lines.pop(0) # remove header
    #    pp.pprint(lines[0])
        for line in lines:
            prot_ids = list()
            line = re.sub('\n$', '', line)
            records = line.split('\t')
    #        print records[0]
            #pp.pprint(records[0])
            if len(records) == 10:
                geneId = records.pop(0)
    #            prot_id = list()
                if geneId not in geneHash:
                    geneHash[geneId] = {'Name':records[0],
                                        'Description':records[1],
                                        'Chromosome': records[2],
                                        'Start': records[3],
                                        'End': records[4],
                                        'Biotype': records[5],
                                        'ProtID': [],
                                        'GO': {}
                                        }
                if geneId in geneHash:
                    if records[6]:
                        prot_ids.append(records[6])                                  
                    geneHash[geneId]['ProtID'].extend(prot_ids)  #records[6]
                    geneHash[geneId]['GO'][records[7]] = records[8]  
    
            if len(records) == 11:
                geneId = records.pop(0)
                if geneId not in geneHash:
                    geneHash[geneId] = {'Name':records[0],
                                        'Description':records[1],
                                        'Chromosome': records[2],
                                        'Start': records[3],
                                        'End': records[4],
                                        'Biotype': records[5],                                    
                                        'GeneID': records[6],
                                        'ProtID': [],
                                        'GO': {}
                                        }
    
                if geneId in geneHash:
                    if records[7]:
                        prot_ids.append(records[7])
                    geneHash[geneId]['ProtID'].extend(prot_ids) #records[6]
                    geneHash[geneId]['GO'][records[8]] = records[9]  
    
            if len(records) == 12:
                geneId = records.pop(0)
                loci = list()
                if geneId not in geneHash:
                    geneHash[geneId] = {'Name':records[0],
                                        'Description':records[1],
                                        'Chromosome': records[2],
                                        'Start': records[3],
                                        'End': records[4],
                                        'Biotype': records[5],                                    
                                        'GeneID': records[6],
                                        'TAIRlocus': '',
                                        'RapID': '',
                                        'TIGRlocus': [],                                     
                                        'ProtID': [],
                                        'GO': {}
                                        }
                        
                if geneId in geneHash:
                    if records[7]:
                        tigr_loci_pattern = re.match(r'^LOC_', records[7]) #(^LOC_)|(^No)
                        rap_pattern = re.match(r'^Os', records[7]) 
                        tair_pattern = re.match(r'^AT', records[7])     
                        if tigr_loci_pattern:
                            loci.append(records[7])
                            geneHash[geneId]['TIGRlocus'].extend(loci)
                        if rap_pattern:
                            geneHash[geneId]['RapID'] = records[7]
                        if tair_pattern:
                            geneHash[geneId]['TAIRlocus'] = records[7]
                    if records[8]:
                        prot_ids.append(records[8])
                        geneHash[geneId]['ProtID'].extend(prot_ids) #records[6]
                    if records[9] and records[10]:          
                        geneHash[geneId]['GO'][records[9]] = records[10]     # for A.thaliana dataset records[9],records[10] contains PO terms and domain respectively  
        fileReader.close()
        return geneHash
    
    def __str__(self):
        return "Gramene Genome Parser"

class GrameneQTLParser(object):
    '''
    classdocs
    '''
    
    
    def __init__(self, infile):
        '''
        Constructor
        '''
        self.infile = infile
    
    def parse(self):
        headers = ['QTLid', 'Name', 'Symbol', 'TOid', 'Category', 'TraitName','TraitSymbol', 'Chromosome', 'Start', 'End']
        qtl_ds = list()

        fileHandle = open(self.infile, "r")
        lines = fileHandle.readlines()
        lines.pop(0) # remove header
        for line in lines:
            line = re.sub('\n$', '', line)
            items = line.split('\t')        
            qtl_ds.append(dict(zip(headers, items)))
          
        return qtl_ds    
        
        
    def __str__(self):
        return "Gramene QTL Parser"

class OryzaBaseParser(object):
    '''
    classdocs
    '''
    
    
    def __init__(self, infile):
        '''
        Constructor
        '''
        self.infile = infile
    
    def parse(self):
        oryGene_ds = dict()
        fileHandle = open(self.infile, "r")
        lines  = fileHandle.readlines()
        lines.pop(0)
    #    pp = pprint.PrettyPrinter(indent=4)
        for current_line in lines:
            current_line = re.sub('\n$', '', current_line)
            records = current_line.split('\t')
            oryGeneID = records.pop(0)
    #        pp.pprint(records)
    #        print oryGeneID + "\t" + records[6] + "\t" + records[7] + "\t" + records[8]         
            oryGene_ds[oryGeneID] = {
                                      "Symbols": [],
                                      "Alt_names": [],
                                      "Name": records[2],
                                      "Alleles": [],
                                      "Chromosome": records[4],
                                      "RAP_id": [],
                                      "Mutant": records[6],
                                      "Arm": records[7],
                                      "Locus": records[8],
                                      "Explanation": records[9],
                                      "Reco_symbol": '',
                                      "Reco_name": '',
                                      "Protein_name" : records[12],
                                      "Trait_class": records[13],
                                      "GO_id": [],
                                      "TO_id": [],
                                      "Gramene_id": []
                                      }
            if records[0]:
                symbols = records[0].split(',')
                symbols = [x.strip() for x in symbols]
                oryGene_ds[oryGeneID]["Symbols"].extend(symbols)
            if records[1]:
                records[1].strip()
                alt_names = records[1].split(',')
                alt_names = [x.strip() for x in alt_names]
                oryGene_ds[oryGeneID]["Alt_names"].extend(alt_names)
            if records[3]:
                alleles = records[3].split(',')
                alleles = [x.strip() for x in alleles]
                oryGene_ds[oryGeneID]["Alleles"].extend(alleles)
            if records[5]:
                rapIds = records[5].split(',')
                oryGene_ds[oryGeneID]["RAP_id"].extend(rapIds)
            if records[10] and records[10] != "_":
                oryGene_ds[oryGeneID]["Reco_symbol"] = records[10]
            if records[11] and records[11] != "_":
                oryGene_ds[oryGeneID]["Reco_name"] = records[11]     
            if records[14]:
                go_ids = re.split(r',', re.sub(':', '_', records[14]))
                oryGene_ds[oryGeneID]["GO_id"].extend(go_ids)
            if records[15]:
                to_ids = re.split(r',', re.sub(':', '_', records[15]))
                oryGene_ds[oryGeneID]["TO_id"].extend(to_ids)        
            if records[16]:
                records[16] = re.sub('\r$', '', records[16])
                gr_ids = re.split(r'/|,', records[16])
                gr_ids = [x.rstrip() for x in gr_ids]
                
                oryGene_ds[oryGeneID]["Gramene_id"].extend(gr_ids)
        
        fileHandle.close() 
        return oryGene_ds            
        
    def __str__(self):
        return "OryzaBase Parser"
            