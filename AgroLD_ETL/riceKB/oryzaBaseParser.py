#!/usr/bin/env python
'''
Created on July 17, 2014
The oryzaBaseParsers module is created as part of the Rice Knowledge Base project.

This module contains Parsers, RDF converters and generic functions for handling OryzaBase data

TODO:
    1) Add documentation
    2) Fix Gramene record trailing space in the parser, now it is being handled in the RDF converter
    3) better Error handling
@author: venkatesan
'''
import pprint
from riceKB.globalVars import *
import re
import os

'''
OryzaBase Fields
        trait_gene_id, 0
        symbol, 1 list (Note!! entries separated by ',', some entries have '[]', '/', '#')
        alternative_name, 2 list
        name_en, 3 
        allele, 4 list
        chromosome_no, 5
        RAP_id, 6 list
        is_mutant, 7
        arm, 8
        locus, 9
        explanation_en, 10
        recommended_gene_symbol, 11 
        recommended_gene_name, 12 (Note!! some entries have '_')
        protein_name, 13 (Note!! some entries have '_')
        Class name en, 14
        Gene Ontology IDs, 15 list
        Trait Ontology IDs, 16 list
        Gramene ID 17 list (Note!! IDs separated by '/' or/and ',')
'''

def oryzaBaseParser(input_file):
    oryGene_ds = dict()
    fileHandle = open(input_file, "r")
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
#    pp.pprint(oryGene_ds)
#        print records[0] + "\t" + records[4] 
    
def oryzaBaseRDF(infile, output_file):
#    pp = pprint.PrettyPrinter(indent=4)

    print "*************Parsing OryzaBase gene data ***********\n" 
    
    orygene_ds = oryzaBaseParser(infile)
    gene_count = len(orygene_ds)
    
    print "Number of genes: %s\n" % (str(gene_count))
    print "OryzaBase gene data has been parsed!\n"
    print "*************************************\n\n"
    
    ttl_handle = open(output_file, "w")
    ttl_buffer = ''
    
    print "************* OryzaBase RDF conversion begins***********\n" 
    
    ttl_handle.write(base + "\t" + "<" + base_uri + "> .\n")    
    ttl_handle.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    ttl_handle.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    ttl_handle.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    ttl_handle.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    ttl_handle.write(pr + "\t" + gr_g_ns + "<" + gramene_gene + "> .\n")
    ttl_handle.write(pr + "\t" + rapdb_ns + "<" + rapdb_uri + "> .\n")
    ttl_handle.write(pr + "\t" + orygene_ns + "<" + orygene_uri + "> .\n\n")
#    pp.pprint(orygene_ds)
    for oryid in orygene_ds:
        ttl_buffer = ''
        
        ttl_buffer += orygene_ns + oryid + "\n"
        ttl_buffer += "\t" + rdf_ns + "type" + "\t" + base_vocab_ns + "Gene" + " ;\n"
        ttl_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + gene_term + " ;\n"
        for item in orygene_ds[oryid]:
            if item == 'Reco_symbol':
                if orygene_ds[oryid][item]:
                    ttl_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (orygene_ds[oryid][item]) + " ;\n"
            if item == 'Reco_name':
                if orygene_ds[oryid][item]:
                    ttl_buffer += "\t" + base_vocab_ns + "description" + "\t" + '"%s"' % (orygene_ds[oryid][item]) + " ;\n"
            if item == 'Name':
                if orygene_ds[oryid][item]:
                    ttl_buffer += "\t" + base_vocab_ns + "name" + "\t" + '"%s"' % (orygene_ds[oryid][item]) + " ;\n"            
            if item == 'Explanation':
                if orygene_ds[oryid][item]:
                    ttl_buffer += "\t" + base_vocab_ns + "explanation" + "\t" + '"%s"' % (orygene_ds[oryid][item]) + " ;\n"
            if item == 'Chromosome':
                if orygene_ds[oryid][item]:
                    ttl_buffer += "\t" + base_vocab_ns + "chromosome" + "\t" + '"%s"' % (orygene_ds[oryid][item]) + " ;\n"                    
            if item == 'Alleles':
                if orygene_ds[oryid][item]:
                    for allele in orygene_ds[oryid][item]:
                        ttl_buffer += "\t" + base_vocab_ns + "has_allele" + "\t" + '"%s"' % (allele) + " ;\n"
            if item == 'Mutant':
                if orygene_ds[oryid][item]:
                    ttl_buffer += "\t" + base_vocab_ns + "has_mutant" + "\t" + '"%s"' % (orygene_ds[oryid][item]) + " ;\n"                    
            if item == 'Arm':
                if orygene_ds[oryid][item]:
                    ttl_buffer += "\t" + base_vocab_ns + "has_chromosome_arm" + "\t" + '"%s"' % (orygene_ds[oryid][item]) + " ;\n"                    
            if item == 'Locus':
                if orygene_ds[oryid][item]:
                    ttl_buffer += "\t" + base_vocab_ns + "has_locus" + "\t" + '"%s"' % (orygene_ds[oryid][item]) + " ;\n"                    
            if item == 'Symbols':
                if orygene_ds[oryid][item]:
                    for symbol in orygene_ds[oryid][item]:
                        ttl_buffer += "\t" + base_vocab_ns + "has_synonym" + "\t" + '"%s"' % (symbol) + " ;\n"                    
            if item == 'Alt_names':
                if orygene_ds[oryid][item]:
                    for alt_name in orygene_ds[oryid][item]:
                        ttl_buffer += "\t" + base_vocab_ns + "has_alternative_name" + "\t" + '"%s"' % (alt_name) + " ;\n"
            if item == 'RAP_id':
                if orygene_ds[oryid][item]:
                    for rap_id in orygene_ds[oryid][item]:
                        ttl_buffer += "\t" + base_vocab_ns + "has_rap_identifier" + "\t" + rapdb_ns + rap_id + " ;\n"                                                                    
            if item == 'Gramene_id':
                if orygene_ds[oryid][item]:
                    for gr_id in orygene_ds[oryid][item]:
                        if gr_id != '':
                            ttl_buffer += "\t" + base_vocab_ns + "has_gramene_identifier" + "\t" + gr_g_ns + gr_id + " ;\n"                                            
            if item == 'Protein_name':
                if orygene_ds[oryid][item]:
                    ttl_buffer += "\t" + base_vocab_ns + "has_protein_name" + "\t" + '"%s"' % (orygene_ds[oryid][item]) + " ;\n"                    
            if item == 'Trait_class':
                if orygene_ds[oryid][item]:
                    ttl_buffer += "\t" + base_vocab_ns + "has_trait_class" + "\t" + '"%s"' % (orygene_ds[oryid][item]) + " ;\n"                    
            if item == 'TO_id':
                if orygene_ds[oryid][item]:
                    for to_term in orygene_ds[oryid][item]:
                        ttl_buffer += "\t" + base_vocab_ns + "has_trait" + "\t" + obo_ns + to_term + " ;\n"                    
            if item == 'GO_id':
                if orygene_ds[oryid][item]:
                    for go_term in orygene_ds[oryid][item]:
                        ttl_buffer += "\t" + base_vocab_ns + "go_term" + "\t" + obo_ns + go_term + " ;\n"                    
            
        ttl_buffer = re.sub(' ;$', ' .', ttl_buffer)
            
        ttl_handle.write(ttl_buffer)
    ttl_handle.close()
    print "************* OryzaBase RDF completed ************!\n\n" 