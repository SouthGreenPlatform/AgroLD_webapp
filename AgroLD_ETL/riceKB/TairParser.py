#!/usr/bin/env python
'''
Created on Aug 27, 2014

@author: venkatesan
'''
import pprint
from riceKB.globalVars import *
from riceKB.globalMods import removeDuplicates
import re
import os 

'''
Parsers
'''

def araCycParser(input_file):
    headers = ['PathwayID', 'PathwayName', 'ReactionID', 'EC', 'ProteinID', 'ProteinName', 'GeneID', 'GeneName']
    pw_ds = list()    

    fileOpener = open(input_file, "r")
    
    print "*****Parsing AraCyc data **********\n"
    
    lines = fileOpener.readlines()
    lines.pop(0)
    
    for line in lines:
        line = re.sub('\n$', '', line)
        records = line.split('\t')
        pw_ds.append(dict(zip(headers, records)))
    return pw_ds #pw_hash
    fileOpener.close()
    print "AraCyc data has been parsed!\n"
    print "**********************************\n\n"

'''
RDF Converter
'''
    
'''
TODO: 1) Add gene information
      2) Check EC code, needs to be appended with ".-" in some cases. Refer: http://www.ebi.ac.uk/miriam/main/collections/MIR:00000004
'''
 
def araCycRDF(pw_ds, output_dir):
    aracyc_ttl = "aracyc.ttl"
    output_file = os.path.join(output_dir, aracyc_ttl)
    rdf_writer = open(output_file, "w")
    
#    pp = pprint.PrettyPrinter(indent=4)
    pw_counter = 0
    unmapped_gene_counter = 0
    pw = {}
#    gene_hash = {}
    prot_hash = {}
    react_hash = {}
    rdf_buffer = ''
    prot_buffer = ''
#    gene_buffer = ''
    react_buffer = ''
    previous_pw_id = ''
    
    print "*************AraCyc RDF conversion begins***********\n"
    
    rdf_writer.write(base + "\t" + "<" + base_uri + "> .\n")
    rdf_writer.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    rdf_writer.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    rdf_writer.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    rdf_writer.write(pr + "\t" + swo_ns + "<" + swo_uri + "> .\n")
    rdf_writer.write(pr + "\t" + sio_ns + "<" + sio_uri + "> .\n")
    rdf_writer.write(pr + "\t" + aracyc_ns + "<" + aracyc_uri + "> .\n")
    rdf_writer.write(pr + "\t" + reaction_ns + "<" + reaction_uri + "> .\n")
    rdf_writer.write(pr + "\t" + aracyc_gene_ns + "<" + aracyc_gene_uri + "> .\n")
    rdf_writer.write(pr + "\t" + aracyc_prot_ns + "<" + aracyc_prot_uri + "> .\n")
    rdf_writer.write(pr + "\t" + tair_l_ns + "<" + tair_l_uri + "> .\n")
    rdf_writer.write(pr + "\t" + ec_code_ns + "<" + ec_code_uri + "> .\n\n")
    
    for record in pw_ds:
        pw_id = record['PathwayID']
        react = record['ReactionID']
        prot = record['ProteinID']
        prot_name = record['ProteinName']
        gene = record['GeneID']
        gene_name = record['GeneName']
        prot_buffer = ''
#        gene_buffer = ''
        react_buffer = ''
        
        if previous_pw_id and pw_id not in previous_pw_id:
            rdf_buffer = re.sub(' ;$', ' .', rdf_buffer)
            rdf_writer.write(rdf_buffer)
            rdf_buffer = ''
        
        if pw_id not in pw:
            pw_counter += 1
            rdf_buffer += aracyc_ns + pw_id + "\n"
            rdf_buffer += "\t" + rdf_ns + "type" + "\t" + base_vocab_ns + "Metabolic_Pathway" + " ;\n" #base_vocab_ns + "Metabolic_Pathway"
            rdf_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + sio_ns + met_pw_sio_term + " ;\n"
            rdf_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + swo_ns + biocyc_pw_term + " ;\n"
            rdf_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (record['PathwayName']) + " ;\n"
            pw[pw_id] = 1
            previous_pw_id = pw_id
        
        if pw_id == previous_pw_id:
            # Reaction
            if react:
                if react not in react_hash:
                    rdf_buffer += "\t" + base_vocab_ns + "has_reaction" + "\t" + reaction_ns + react + " ;\n"
                    react_buffer += reaction_ns + react + "\n"
                    react_buffer += "\t" + rdf_ns + "type" + "\t" + base_vocab_ns + "Reaction" + " ;\n" 
                    react_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + swo_ns + biocyc_react_term + " ;\n"
                    if record['EC'] != '-':
                        ec = re.sub('^EC-', '', record['EC'])
                        react_buffer += "\t" + base_vocab_ns + "has_ec_code" + "\t" + ec_code_ns + ec + " .\n"
                    else: 
                        react_buffer = re.sub(' ;$', ' .', react_buffer)
                    rdf_writer.write(react_buffer)
                    react_hash[react] = 1
            # Protein    
            if prot != 'unknown': # and prot_name != 'unknown'
                rdf_buffer += "\t" + base_vocab_ns + "has_agent" + "\t" + aracyc_prot_ns + prot + " ;\n"
                if prot not in prot_hash:
                    prot_buffer += aracyc_prot_ns + prot + "\n"
                    prot_buffer += "\t" + rdf_ns + "type" + "\t" + base_vocab_ns + "Protein" + " ;\n"
                    prot_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (prot_name) + " ;\n"
                    if gene != 'unknown': # and gene_name != 'unknown'
                        pattern = re.compile(r'^AT[1-5]G\d{5}')
                        if pattern.match(gene):
                            prot_buffer += "\t" + base_vocab_ns + "encoded_by" + "\t" + tair_l_ns + gene + " ;\n"
#                            gene_hash[gene] = 1
                        elif pattern.search(gene_name):  #pattern.match(gene_split):
                            gene_split = gene_name[:9]
                            prot_buffer += "\t" + base_vocab_ns + "encoded_by" + "\t" + tair_l_ns + gene_split + " ;\n"
#                            gene_hash[gene_split] = 1
                        else:
                            prot_buffer += "\t" + base_vocab_ns + "encoded_by" + "\t" + aracyc_gene_ns + gene + " ;\n"
                            print "unmapped gene ID and gene name: %s %s\n" % (gene, gene_name)
                            unmapped_gene_counter += 1
#                            continue
                        prot_buffer = re.sub(' ;$', ' .', prot_buffer)                            
                    else:
                        prot_buffer = re.sub(' ;$', ' .', prot_buffer)
                    prot_hash[prot] = 1
                    rdf_writer.write(prot_buffer)
                                    
        previous_pw_id = pw_id        
#    pp.pprint(gene_hash)                                                           
    if previous_pw_id:
        rdf_buffer = re.sub(' ;$', ' .', rdf_buffer)
        rdf_writer.write(rdf_buffer)
        
    rdf_writer.close()
    print "Total number of unmapped genes: %s\n" % (str(unmapped_gene_counter))    
    print "Number of AraCyc pathways: %s\n" % (str(pw_counter))
    print "******************Aracyc RDF completed***********************\n"
        