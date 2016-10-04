#!/usr/bin/env python
'''
Created on Jun 19, 2014
The grameneParsers module is created as part of the Rice Knowledge Base project.

This module contains Parsers, RDF converters and generic tools for handling Gramene data

TODO:
    1) Add documentation
    2) better Error handling
@author: venkatesan
'''
import pprint
from globalVars import *
import re
import os 
#from __builtin__ import map

'''
 Parsers 
''' 
'''
def genomeParser(infile):
    pp = pprint.PrettyPrinter(indent=4)
    geneHash = {}

    fileReader = open(infile, "r")
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
  '''


def geneParser(infile):
    
#    pp = pprint.PrettyPrinter(indent=4)
    gene_hash = {}
    tigr_pattern = re.compile(r'^LOC\_Os\d{1,2}g\d{5}\.\d$')
    rap_pattern = re.compile(r'^Os\d{2}g\d{7}$')
    tair_pattern = re.compile(r'^AT[1-5]G\d{5}$')
    prot_pattern = re.compile(r'^([A-N,R-Z][0-9]([A-Z][A-Z, 0-9][A-Z, 0-9][0-9]){1,2})|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])(\.\d+)?$')
    ont_pattern = re.compile(r'^\w+\:\d{7}$')
    
    file_reader = open(infile, "r")
    lines = file_reader.readlines()
    lines.pop(0) # remove header
    
    for line in lines:
        line = re.sub('\n$', '', line)
        records = line.split('\t')
        gene_id = records.pop(0)
        # Building data structure
        if gene_id not in gene_hash:
            gene_hash[gene_id] = {
                                  'Name': records[0],
                                  'Description': records[1],
                                  'Chromosome': records[2],
                                  'Start': records[3],
                                  'End': records[4],
                                  'Biotype': records[5],                                    
                                  'RapID': '',
                                  'TairLocus': '',
                                  'TIGRlocus': {},
                                  'ProtID': {},
                                  'Ontology': {}
                                  }
        
        if gene_id in gene_hash:
            # Records of sps: A.thaliana            
            if len(records) == 11:
                if records[6]:
                    gene_hash[gene_id]['TairLocus'] = records[6]
                if records[7]:
                    gene_hash[gene_id]['ProtID'][records[7]] = '-'
#                    prot_list.append(records[7])
#                    gene_hash[gene_id]['ProtID'].extend(prot_list)
                if records[8]:
                    gene_hash[gene_id]['ProtID'][records[8]] = '-'
#                    prot_list.append(records[8])
#                    gene_hash[gene_id]['ProtID'].extend(prot_list)
                if records[9]:
                    gene_hash[gene_id]['Ontology'][records[9]] = records[10]
            # Records of sps: S.bicolor
            if len(records) == 10:
                if records[6]:
                    gene_hash[gene_id]['ProtID'][records[6]] = '-'
#                    prot_list.append(records[6])
#                    gene_hash[gene_id]['ProtID'].extend(prot_list)
                if records[7]:
                    gene_hash[gene_id]['ProtID'][records[7]] = '-'
#                    prot_list.append(records[7])
#                    gene_hash[gene_id]['ProtID'].extend(prot_list)
                if records[8]:
                    gene_hash[gene_id]['Ontology'][records[8]] = records[9]
            # Records of sps: O.barthii, O.meridionalis, O.s.indica, O.s.japonica 
            if len(records) == 9:
                if records[6]:
                    if tigr_pattern.match(records[6]):
                        gene_hash[gene_id]['TIGRlocus'][records[6]] = '-'
#                        tigr_loci.append(records[6])
#                        gene_hash[gene_id]['TIGRlocus'].extend(tigr_loci)
                    if rap_pattern.match(records[6]):
                        gene_hash[gene_id]['RapID'] = records[6]
#                    elif tair_pattern.match(records[6]):
#                        gene_hash[gene_id]['Tairlocus'] = records[6]
                    if prot_pattern.match(records[6]):
                        gene_hash[gene_id]['ProtID'][records[6]] = '-'
#                        prot_list.append(records[6])
#                        gene_hash[gene_id]['ProtID'].extend(prot_list)
                if records[7]:
                    if ont_pattern.match(records[7]):
                        gene_hash[gene_id]['Ontology'][records[7]] = records[8]
                    else:
                        gene_hash[gene_id]['ProtID'][records[7]] = '-'
#                        prot_list.append(records[7])
#                        gene_hash[gene_id]['ProtID'].extend(prot_list)
                if records[8]:
                    if prot_pattern.match(records[8]):
                        gene_hash[gene_id]['ProtID'][records[8]] = '-'
#                        prot_list.append(records[8])
#                        gene_hash[gene_id]['ProtID'].extend(prot_list)
            # Records of sps: O.glaberrima, T.aestivum, T.urartu, Z.mays 
            if len(records) == 8:
                if records[6]:
                    gene_hash[gene_id]['ProtID'][records[6]] = '-'
#                    prot_list.append(records[6])
#                    gene_hash[gene_id]['ProtID'].extend(prot_list)
                if records[7]:
                    gene_hash[gene_id]['ProtID'][records[7]] = '-'
#                    prot_list.append(records[7])
#                    gene_hash[gene_id]['ProtID'].extend(prot_list)
            #  Records of sps: O.brachyantha
            if len(records) == 7:
                if records[6]:
                    gene_hash[gene_id]['ProtID'][records[6]] = '-'
#                    prot_list.append(records[6])
#                    gene_hash[gene_id]['ProtID'].extend(prot_list)
                                                       
    return gene_hash                    
    file_reader.close()


def qtlParser(infile):
    headers = ['QTLid', 'Name', 'Symbol', 'TOid', 'Category', 'TraitName','TraitSymbol', 'Chromosome', 'Start', 'End']
    qtl_ds = list()
#    pp = pprint.PrettyPrinter(indent=4)
     
    fileHandle = open(infile, "r")
    lines = fileHandle.readlines()
    lines.pop(0) # remove header
    for line in lines:
        line = re.sub('\n$', '', line)
        items = line.split('\t')        
        qtl_ds.append(dict(zip(headers, items)))
    
    fileHandle.close()  
    return qtl_ds    
    
'''
def riceCyc(input_files):
    headers = ['Gene', 'Name', 'ReationId', 'ReactionName', 'EC', 'PathwayId', 'PathwayName']
    
    pw_ds = list()

    
    
    for input_file in input_files:
        fileHandle = open(input_file, "r")
    
#    print "*****Parsing RiceCyc data **********\n"
           
        lines = fileHandle.readlines()
        lines.pop(0)    
        for line in lines:
            line = re.sub('\n$', '', line)
            records = line.split('\t')
            pw_ds.append(dict(zip(headers, records)))
    
#    pw_ds.sort(key=lambda x: (x['PathwayId'], x['ReationId']))
    
    fileHandle.close()
    return pw_ds
    print "AraCyc data has been parsed!\n"
    print "**********************************\n\n"
'''

def CycParser(in_files):
    pp = pprint.PrettyPrinter(indent=4)
    pw_datastucture = {}
        
    for in_file in in_files:
        fileHandle = open(in_file, "r")
        lines = fileHandle.readlines()
        lines.pop(0)

        for line in lines:
#            ec_codes = {} #list()
#            reactions = {}
            pathways = {} 
            line = re.sub('\n$', '', line)
            records = line.split('\t')
            gene_id = records.pop(0)
            

#            reactions[records[1]] = {'ReactionName': records[2], 'EC': {records[3]: '-'}}
            
#            pathways[records[4]] = {'PathwayName': records[5], 'Reactions': reactions}
#            if records[4] not in pathways:
#                pathways[records[4]] = {'PathwayName': records[5], 'Reactions': {}}
#            if records[4] in pathways:
#                pathways[records[4]]['Reactions'].update(reactions)

            pathways[records[4]] = records[5]
            if gene_id in pw_datastucture:
                pw_datastucture[gene_id]['Pathways'].update(pathways)#= {records[4]: {'PathwayName': records[5], 'Reactions': []} }
                
            if gene_id not in pw_datastucture:
                pw_datastucture[gene_id] = {
                                            'Name': records[0],
                                            'Pathways': {}
                                            }
            if gene_id in pw_datastucture:
                pw_datastucture[gene_id]['Pathways'].update(pathways)#= {records[4]: {'PathwayName': records[5], 'Reactions': []} }
#            pp.pprint(reactions) #reactions pathways
    fileHandle.close()
    return pw_datastucture             
      
''' 
 RDF Converters 
'''             
def grameneGeneRDF(files, output_dir):
    rdf_buffer = ''
#    geneId_prefix = ''
#    tigr_prefix = ''
#    rapdb_prefix = ''
#    tair_prefix = ''
    gene_counter = 0
#    pp = pprint.PrettyPrinter(indent=4)
    turtle_file = "gramene_genes.ttl"
    output_file = os.path.join(output_dir, turtle_file)
    output_opener = open(output_file, "w")
    
    # Printing Prefixes
    output_opener.write(base + "\t" + "<" + base_uri + "> .\n")
    output_opener.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    output_opener.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    output_opener.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    output_opener.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    output_opener.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    output_opener.write(pr + "\t" + ensembl_ns + "<" + ensembl_plant + "> .\n")
    output_opener.write(pr + "\t" + rapdb_ns + "<" + rapdb_uri + "> .\n")
    output_opener.write(pr + "\t" + tigr_ns + "<" + tigr_uri + "> .\n")
    output_opener.write(pr + "\t" + tair_l_ns + "<" + tair_l_uri + "> .\n")                
    output_opener.write(pr + "\t" + up_ns + "<" + uniprot + "> .\n\n")
    '''
    Ajout du prefix pour la realese des donnees
    '''
    output_opener.write(pr + "\t" + res_ns + "<" + resource + "> .\n\n")

    
    for gene_file in files:
        rdf_buffer = ''
#        tigr_prefix = ''
#        rapdb_prefix = ''
#        geneId_prefix = ''
#        tair_prefix = ''
        
        output_file_name = os.path.split(os.path.splitext(gene_file)[0])[1]
#        turtle_file = "gramene_" + output_file_name + "_genes" + ".ttl"
#        output_file = os.path.join(output_dir, turtle_file)
#        output_file = "gramene_genes.ttl"
#        output_opener = open(output_file, "w")
        
        print "*************Parsing %s genome data ***********\n" % (output_file_name)
        
        gene_ds = geneParser(gene_file)
#        gene_ds = genomeParser(gene_file)
#        pp.pprint(gene_ds)

#        print "%s data has been parsed!\n" % (output_file_name)
#        print "*************************************\n\n"
        
        print "************* %s RDF conversion begins***********\n" % (output_file_name)
        
        for gene_id in gene_ds:
            gene_counter += 1
            rdf_buffer += ensembl_ns + gene_id + "\n"
            rdf_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Gene" + " ;\n"
            #rdf_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            #rdf_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + gene_term + " ;\n"
            for tax_id in taxon_ids:
                if output_file_name == taxon_ids[tax_id]:
                    rdf_buffer += "\t" + base_vocab_ns + "taxon" + "\t" + obo_ns + "NCBITaxon_" + tax_id + " ;\n"
            for record_item in gene_ds[gene_id]:
                if record_item == 'Name':
                    if gene_ds[gene_id][record_item]: 
                        rdf_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (gene_ds[gene_id][record_item]) + " ;\n"
                if record_item == 'Description':
                    if gene_ds[gene_id][record_item]: 
                        rdf_buffer += "\t" + base_vocab_ns + "description" + "\t" + '"%s"' % (gene_ds[gene_id][record_item].replace("'", "")) + " ;\n"
                if record_item == 'Chromosome':
                    rdf_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t" + '"%s"' % (gene_ds[gene_id][record_item]) + " ;\n" 
                if record_item == 'Start':
                    rdf_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + '"%s"' % (gene_ds[gene_id][record_item]) + " ;\n"
                if record_item == 'End':
                    rdf_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + '"%s"' % (gene_ds[gene_id][record_item]) + " ;\n"
                if record_item == 'Biotype':
                    rdf_buffer += "\t" + base_vocab_ns + "has_biotype" + "\t" + '"%s"' % (gene_ds[gene_id][record_item]) + " ;\n"
                if record_item == 'ProtID':
                    if gene_ds[gene_id][record_item]:
                        proteins = gene_ds[gene_id][record_item].keys()
                        for protein in proteins:
                            rdf_buffer += "\t" + base_vocab_ns + "encodes" + "\t" + up_ns + protein + " ;\n"
                if record_item == 'RapID':
                    if gene_ds[gene_id][record_item]:
                        rdf_buffer += "\t" + base_vocab_ns + "has_dbxref" + "\t" + rapdb_ns + gene_ds[gene_id][record_item] + " ;\n"
                if record_item == 'TairLocus':
                    if gene_ds[gene_id][record_item]:
                        rdf_buffer += "\t" + base_vocab_ns + "has_dbxref" + "\t" + tair_l_ns + gene_ds[gene_id][record_item] + " ;\n"                          
                if record_item == 'TIGRlocus':
                    if gene_ds[gene_id][record_item]:
#                        tigr_prefix = pr + "\t" + tigr_ns + "<" + tigr_uri + "> .\n"
                        tigr_loci = gene_ds[gene_id][record_item].keys()
                        for locus in tigr_loci:
                            rdf_buffer += "\t" + base_vocab_ns + "has_dbxref" + "\t" + tigr_ns + locus + " ;\n"
                if record_item == 'Ontology':
                    if gene_ds[gene_id][record_item]:
                        ont_terms = gene_ds[gene_id][record_item].items()
                        for ont in ont_terms:
                            ont_id = ont[0].replace(":", "_")
                            if ont[1] == 'molecular_function':
                                rdf_buffer += "\t" + obo_ns + "has_function" + "\t" + obo_ns + ont_id + " ;\n"
                            if ont[1] == 'biological_process':
                                rdf_buffer += "\t" + obo_ns + "participates_in" + "\t" + obo_ns + ont_id + " ;\n"
                            if ont[1] == 'cellular_component':
                                rdf_buffer += "\t" + obo_ns + "located_in" + "\t" + obo_ns + ont_id + " ;\n"
                            if ont[1] == 'plant_anatomy':
                                rdf_buffer += "\t" + base_vocab_ns + "expressed_in" + "\t" + obo_ns + ont_id + " ;\n"
                            if ont[1] == 'plant_structure_development_stage':
                                rdf_buffer += "\t" + base_vocab_ns + "expressed_at" + "\t" + obo_ns + ont_id + " ;\n"
                
            rdf_buffer = re.sub(' ;$', ' .', rdf_buffer)
            
        output_opener.write(rdf_buffer)
    output_opener.close()
#        if geneId_prefix:
#            output_opener.write(geneId_prefix)
#        if tigr_prefix:
#            output_opener.write(tigr_prefix)
#        if rapdb_prefix:
#            output_opener.write(rapdb_prefix)
#        if tair_prefix:
#            output_opener.write(tair_prefix)                            
        
    print "Number of genes in %s are: %s\n" % (output_file_name, str(gene_counter))
#    print "************* %s RDF completed ************\n" % (output_file_name)
#    print "Gramene gene data has been converted to RDF!\n"


def grameneQTLRDF(infile, output_dir):
    qtl_buffer = '' 
    to_hash = dict()
    qtl_counter = 0   
    turtle_file_name = "gramene.qtl.ttl"
    outfile = os.path.join(output_dir, turtle_file_name)
    outHandle = open(outfile, "w")
    
    print "*********** Parsing Gramene QTL data ***************\n"
    
    qtl_ds = qtlParser(infile)
    
#    print "Gramene QTL data has been parsed!\n"
#    print "*************************************\n" 
    
    print "************* Gramene QTL RDF conversion begins***********\n"
    
    outHandle.write(base + "\t" + "<" + base_uri + "> .\n")
    outHandle.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    outHandle.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    outHandle.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    outHandle.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    outHandle.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    outHandle.write(pr + "\t" + gr_qtl_ns + "<" + gramene_qtl + "> .\n\n")

    '''
    Ajout du prefix pour la realese des donnees
    '''
    outHandle.write(pr + "\t" + res_ns + "<" + resource + "> .\n\n")


    for records in qtl_ds:
        qtl_buffer = ''
        qtl_counter += 1
        chrm = records['Chromosome'].replace("Chr. ", "")
        to_id = records['TOid'].replace(":", "_")
        
        qtl_buffer += gr_qtl_ns + records['QTLid'] + "\n"
        qtl_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "QTL" + " ;\n"
        #qtl_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
        #qtl_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + qtl_term + " ;\n"
        qtl_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (records['Name']) + " ;\n"
        if records['Symbol']:
            qtl_buffer += "\t" + base_vocab_ns + "has_symbol" + "\t" + '"%s"' % (records['Symbol']) + " ;\n"
        qtl_buffer += "\t" + base_vocab_ns + "is_located_on" + "\t" + '"%s"' % (chrm) + " ;\n"
        qtl_buffer += "\t" + base_vocab_ns + "has_start_position" + "\t" + '"%s"' % (records['Start']) + " ;\n"
        qtl_buffer += "\t" + base_vocab_ns + "has_end_position" + "\t" + '"%s"' % (records['End']) + " ;\n"
        qtl_buffer += "\t" + base_vocab_ns + "has_trait" + "\t" + obo_ns + to_id + " ;\n"
            
#        if to_id not in to_hash:
#            outHandle.write(obo_ns + to_id + "\n")
#            outHandle.write("\t" + rdf_ns + "type" + "\t" + obo_ns + plant_trait_term + " ;\n") #base_vocab_ns + "Concept"
#            outHandle.write("\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + plant_trait_term + " ;\n")
#            outHandle.write("\t" + rdfs_ns + "label" + "\t" + '"%s"' % (records['TraitName']) + " ;\n")
#            outHandle.write("\t" + base_vocab_ns + "has_symbol" + "\t" + '"%s"' % (records['TraitSymbol']) + " ;\n")
#            outHandle.write("\t" + base_vocab_ns + "has_category" + "\t" + '"%s"' % (records['Category']) + " .\n")
#            to_hash[to_id] = 1
        qtl_buffer = re.sub(' ;$', ' .', qtl_buffer)
        outHandle.write(qtl_buffer)
    outHandle.close()
    print "Number of QTLs: %s\n" % (str(qtl_counter))
    print "********* Gramene QTL RDF completed ***********\n"

def CycRDF(data_stuc, output_dir):
    pw_hash = {}
#    react_hash = {}
#    gene_hash = {} 
#    previous_gene_id = ''
#    pw_buffer = ''
    gene_buffer = ''
#    react_buffer = ''
    pw_counter = 0
    gene_counter = 0
    tigr_pattern = re.compile(r'^LOC\_OS\d{1,2}G\d{5}\.\d$')
    sorghum_pattern = re.compile(r'^SB\d{2}G\d{6}\.\d$')
    alt_sorghum_match = re.compile(r'^SB\d{4}S\d{6}\.\d$')
    arabidopsis_pattern = re.compile(r'^AT[1-5]G\d{5}$')
    maize_pattern = re.compile(r'^GRMZM\d{1}G\d{6}')
    alt_maize_match = re.compile(r'^\w+\d{6}\.\d{1}\_\w+\d{3}')
    
    cyc_turtle = "gramene.cyc.ttl"
    outfile = os.path.join(output_dir, cyc_turtle)
    outputWriter = open(outfile, "w")
    
    print "*************Cyc RDF conversion begins***********\n"
    
    outputWriter.write(base + "\t" + "<" + base_uri + "> .\n")
    outputWriter.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    outputWriter.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    outputWriter.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    outputWriter.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    outputWriter.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    outputWriter.write(pr + "\t" + swo_ns + "<" + swo_uri + "> .\n")
    outputWriter.write(pr + "\t" + sio_ns + "<" + sio_uri + "> .\n")
    outputWriter.write(pr + "\t" + pathway_ns + "<" + pathway_uri + "> .\n")
#    outputWriter.write(pr + "\t" + reaction_ns + "<" + reaction_uri + "> .\n")
    outputWriter.write(pr + "\t" + tigr_ns + "<" + tigr_uri + "> .\n")
    outputWriter.write(pr + "\t" + tigr_g_ns + "<" + tigr_g_uri + "> .\n")
    outputWriter.write(pr + "\t" + ensembl_ns + "<" + ensembl_plant + "> .\n\n")
#    outputWriter.write(pr + "\t" + ec_code_ns + "<" + ec_code_uri + "> .\n\n")

    '''
    Ajout du prefix pour la realese des donnees
    '''
    outputWriter.write(pr + "\t" + res_ns + "<" + resource + "> .\n\n")


    # Genes
    for gene in data_stuc:
        gene_buffer = ''
        gene_counter += 1
        
        # Data from RiceCyc
        if tigr_pattern.match(gene):
            r_locus = list(gene)
            r_locus[5] = "s"
            r_locus[8] = "g"
            gene_locus = "".join(r_locus)            
            gene_buffer += tigr_ns + gene_locus + "\n"
            gene_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Gene" + " ;\n"
            #gene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            #gene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + mrna_term + " ;\n"
            gene_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (data_stuc[gene]['Name']) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "taxon" + "\t" + obo_ns + "NCBITaxon_" + "39947" + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "develops_from" + "\t" + tigr_g_ns + gene_locus[:14] + " ;\n"
            for pw in data_stuc[gene]['Pathways']:
                gene_buffer += "\t" + base_vocab_ns + "is_agent_in" + "\t" + pathway_ns + pw + " ;\n"
                pw_hash[pw] = data_stuc[gene]['Pathways'][pw]

        # Data from SorghumCyc        
        if sorghum_pattern.match(gene):
            s_g_id = list(gene)
            s_g_id[1] = "b"
            s_g_id[4] = "g"
            sorghum_gene_locus = "".join(s_g_id)            
            gene_buffer += ensembl_ns + sorghum_gene_locus + "\n"
            gene_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Gene" + " ;\n"
            #gene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            #gene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + mrna_term + " ;\n"
            gene_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (data_stuc[gene]['Name']) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "taxon" + "\t" + obo_ns + "NCBITaxon_" + "4558" + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "develops_from" + "\t" + ensembl_ns + sorghum_gene_locus[:11] + " ;\n"
            for pw in data_stuc[gene]['Pathways']:
                gene_buffer += "\t" + base_vocab_ns + "is_agent_in" + "\t" + pathway_ns + pw + " ;\n"
                pw_hash[pw] = data_stuc[gene]['Pathways'][pw]
        if alt_sorghum_match.match(gene):
            alt_s_g_id = list(gene)
            alt_s_g_id[1] = "b"
            alt_s_g_id[6] = "s"
            alt_sorghum_gene_locus = "".join(alt_s_g_id)
            gene_buffer += ensembl_ns + alt_sorghum_gene_locus + "\n"
            gene_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Gene" + " ;\n"
            #gene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            #gene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + mrna_term + " ;\n"
            gene_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (data_stuc[gene]['Name']) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "taxon" + "\t" + obo_ns + "NCBITaxon_" + "4558" + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "develops_from" + "\t" + ensembl_ns + alt_sorghum_gene_locus[:13] + " ;\n"
            for pw in data_stuc[gene]['Pathways']:
                gene_buffer += "\t" + base_vocab_ns + "is_agent_in" + "\t" + pathway_ns + pw + " ;\n"
                pw_hash[pw] = data_stuc[gene]['Pathways'][pw]

        #Data from MaizeCyc                    
        if maize_pattern.match(gene):
            gene_buffer += ensembl_ns + gene + "\n"
            gene_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Gene" + " ;\n"
            #gene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            #gene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + gene_term + " ;\n"
            gene_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (data_stuc[gene]['Name']) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "taxon" + "\t" + obo_ns + "NCBITaxon_" + "4577" + " ;\n"
            for pw in data_stuc[gene]['Pathways']:
                gene_buffer += "\t" + base_vocab_ns + "is_agent_in" + "\t" + pathway_ns + pw + " ;\n"
                pw_hash[pw] = data_stuc[gene]['Pathways'][pw]
        if alt_maize_match.match(gene):
            gene_buffer += ensembl_ns + gene + "\n"
            gene_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Gene" + " ;\n"
            #gene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            #gene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + gene_term + " ;\n"
            gene_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (data_stuc[gene]['Name']) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "taxon" + "\t" + obo_ns + "NCBITaxon_" + "4577" + " ;\n"
            for pw in data_stuc[gene]['Pathways']:
                gene_buffer += "\t" + base_vocab_ns + "is_agent_in" + "\t" + pathway_ns + pw + " ;\n"
                pw_hash[pw] = data_stuc[gene]['Pathways'][pw]

        # Data from AraCyc (Gramene)
        if arabidopsis_pattern.match(gene): 
            gene_buffer += ensembl_ns + gene + "\n"
            gene_buffer += "\t" + rdf_ns + "type" + "\t" + res_ns + "Gene" + " ;\n"
            #gene_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            #gene_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + gene_term + " ;\n"
            gene_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (data_stuc[gene]['Name']) + " ;\n"
            gene_buffer += "\t" + base_vocab_ns + "taxon" + "\t" + obo_ns + "NCBITaxon_" + "3702" + " ;\n"
            for pw in data_stuc[gene]['Pathways']:
                gene_buffer += "\t" + base_vocab_ns + "is_agent_in" + "\t" + pathway_ns + pw + " ;\n"
                pw_hash[pw] = data_stuc[gene]['Pathways'][pw]
        
        gene_buffer = re.sub(' ;$', ' .', gene_buffer)        
        outputWriter.write(gene_buffer)

    #Pathways
    for pw_id in pw_hash:
        pw_counter += 1
        outputWriter.write(pathway_ns +  pw_id + "\n")
        outputWriter.write("\t" + rdf_ns + "type" + "\t" + res_ns + "Pathway_Identifier" + " ;\n")
        #outputWriter.write("\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n")
        #outputWriter.write("\t" + rdfs_ns + "subClassOf" + "\t" + sio_ns + met_pw_sio_term + " ;\n")
        #outputWriter.write("\t" + rdfs_ns + "subClassOf" + "\t" + swo_ns + biocyc_pw_term + " ;\n")
        outputWriter.write("\t" + rdfs_ns + "label" + "\t" + '"%s"' % (pw_hash[pw_id]) + " .\n")
        
    outputWriter.close()
    
    print "Number pathways and genes: %s and %s\n" % (str(pw_counter), str(gene_counter))
    print "******* Cyc RDF completed **********\n" 

    
'''
 Tools                    
'''        
def removeDuplicates(in_list):
    newlist = list(set(in_list))
    return newlist