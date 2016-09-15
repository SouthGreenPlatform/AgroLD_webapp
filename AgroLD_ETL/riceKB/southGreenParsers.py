'''
Created on Sep 18, 2014

@author: venkatesan
'''
import pprint
from globalVars import *
import re
import os 



'''
Parsers
'''
def otlParser(otl_file):
    
    '''
    OTL DS
    
    LineID
    Dev stage
    Dev ID (GRO)
    Gen Observation
    No. Indiv
    Total Number
    Pl anatomy
    Pl anatomy ID
    Trait name 
    Trait ID (TO)
    Name
    keywords
    Description
    known_mutant
    Mutant abreviation
    phenotype class
    phenotype subclass
    
    '''
    map_reader = open(otl_file, "r")
    
    headers = ['LineID', 'DevelopmentStage', 'DevelopmentID', 'Observation', 'NumberIndividuals',
               'TotalNumber','Anatomy', 'AnatomyID', 'TraitName', 'TraitID', 'Name', 'Keywords',
               'Description', 'Mutants', 'MutantAbbreviation', 'PhynotypeClass', 'PhynotypeSubClass']
    
    # Data Structure for Map file
    map_ds = list()
    print "*****Parsing OryzaTagLine data **********\n"
    lines = map_reader.readlines()
    lines.pop(0) # remove header
#    pp.pprint(lines[0])
    for line in lines:
        line = re.sub('\n$', '', line)
        records = line.split(';') #('\t')
        map_ds.append(dict(zip(headers, records)))
            
    return map_ds
    map_reader.close()
    print "OryzaTagLine data has been parsed!\n"
    print "*************************************\n\n"

        
'''
RDFConverter
'''
'''
TODO: 1) check for the KeyError: messages
'''
def otlRDF(otl_ds, output_file):
       
#    mapper = {'GRO:0007045': {'PO:0007038': 'whole plant fruit ripening complete stage'},
#              'GRO:0005339': {'PO:0009010': 'seed'},
#              'GRO:0005361': {'PO:0009089': 'endosperm'},
#              'GRO:0005306': {'PO:0009008': 'plant organ'},              
#              }
    
#    ont_hash = {}
    otl_buffer = ''
    germplasm_counter = 0
    ont_pattern = re.compile(r'^\w+\:\d{7}$')
    rdf_writer = open(output_file, "w")
    
    print "*************OryzaTagLine RDF conversion begins***********\n"
    
    rdf_writer.write(base + "\t" + "<" + base_uri + "> .\n")
    rdf_writer.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    rdf_writer.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    rdf_writer.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")
    rdf_writer.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    rdf_writer.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    rdf_writer.write(pr + "\t" + otl_ns + "<" + otl_uri + "> .\n")
    rdf_writer.write(pr + "\t" + co_ns + "<" + co_uri + "> .\n")
    rdf_writer.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n\n")

    for record in otl_ds:
        otl_buffer = ''
        germplasm_counter += 1
        lineID = record['LineID']
        otl_buffer += otl_ns + lineID + "\n"
        otl_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n" #base_vocab_ns + "Germplasm" + " ;\n"
        otl_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + co_ns + germplasm_term + " ;\n"
        if 'Description' in record.keys():
            if record['Description']:
                otl_buffer += "\t" + base_vocab_ns + "description" + "\t" + '"%s"' % (record['Description']) + " ;\n"
                
        if 'NumberIndividuals' in record.keys():
            if record['NumberIndividuals']: 
                otl_buffer += "\t" + base_vocab_ns + "has_number_of_individuals" + "\t" + '"%s"' % (record['NumberIndividuals']) + "^^" + xsd_ns + "integer" + " ;\n"

        if 'TotalNumber' in record.keys():
            if record['TotalNumber']: 
                otl_buffer += "\t" + base_vocab_ns + "has_total_number" + "\t" + '"%s"' % (record['TotalNumber']) + "^^" + xsd_ns + "integer" + " ;\n"
                
        if 'MutantAbbreviation' in record.keys():
            if record['MutantAbbreviation']:
                otl_buffer += "\t" + base_vocab_ns + "abbreviation" + "\t" + '"%s"' % (record['MutantAbbreviation']) + " ;\n"
                
        if 'Mutants' in record.keys():
            if record['Mutants']:
                otl_buffer += "\t" + base_vocab_ns + "has_mutant_type" + "\t" + '"%s"' % (record['Mutants']) + " ;\n"
                
        if 'Name' in record.keys():
            if record['Name']:
                otl_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (record['Name']) + " ;\n"
                
        if 'Observation' in record.keys():
            if record['Observation']:
                otl_buffer += "\t" + base_vocab_ns + "observation" + "\t" + '"%s"' % (record['Observation']) + " ;\n"
                
        if 'PhynotypeClass' in record.keys():
            if record['PhynotypeClass']:
                otl_buffer += "\t" + base_vocab_ns + "has_phenotype_class" + "\t" + '"%s"' % (record['PhynotypeClass']) + " ;\n"
        
        if 'PhynotypeSubClass' in record.keys():
            if record['PhynotypeSubClass']:
                otl_buffer += "\t" + base_vocab_ns + "has_phenotype_subclass" + "\t" + '"%s"' % (record['PhynotypeSubClass']) + " ;\n"

        if 'Keywords' in record.keys():                    
            if record['Keywords']:
                multiple_words = re.search(",", record['Keywords'])
                if multiple_words:
                    keywords = record['Keywords'].split(',')
                    for word in keywords:
                        otl_buffer += "\t" + base_vocab_ns + "classified_with" + "\t" + '"%s"' % (word) + " ;\n"
                else:
                    otl_buffer += "\t" + base_vocab_ns + "classified_with" + "\t" + '"%s"' % (record['Keywords']) + " ;\n"
        
        if 'DevelopmentID' in record.keys():     
            if record['DevelopmentID']:
                dev_id = record['DevelopmentID']#.replace(":", "_")
                if ont_pattern.match(dev_id):
                    dev_id = dev_id.replace(":", "_")
                    otl_buffer += "\t" + base_vocab_ns + "expressed_at" + "\t" + obo_ns + dev_id + " ;\n"
#                if dev_id not in ont_hash:
#                    rdf_writer.write(obo_ns + dev_id + "\n")
    #                rdf_writer.write("\t" + rdf_ns + "type" + "\t" + obo_ns + plant_dev_term + " ;\n")
#                    if record['DevelopmentStage']:
#                        rdf_writer.write("\t" + rdf_ns + "type" + "\t" + obo_ns + plant_dev_term + " ;\n")                
#                        rdf_writer.write("\t" + rdfs_ns + "label" + "\t" + '"%s"' % (record['DevelopmentStage']) + " .\n")
#                    else:
#                        rdf_writer.write("\t" + rdf_ns + "type" + "\t" + obo_ns + plant_dev_term + " .\n")
#                    ont_hash[dev_id] = 1
        
        if 'AnatomyID' in record.keys():        
            if record['AnatomyID']:
                anatomy_id = record['AnatomyID']#.replace(":", "_")
                if ont_pattern.match(anatomy_id):
                    anatomy_id = anatomy_id.replace(":", "_")
                    otl_buffer += "\t" + base_vocab_ns + "expressed_in" + "\t" + obo_ns + anatomy_id + " ;\n"
#                if anatomy_id not in ont_hash:
#                    rdf_writer.write(obo_ns + anatomy_id + "\n")
    #                rdf_writer.write("\t" + rdf_ns + "type" + "\t" + obo_ns + plant_anatomy_term + " ;\n")
#                    if record['Anatomy']:
#                        rdf_writer.write("\t" + rdf_ns + "type" + "\t" + obo_ns + plant_anatomy_term + " ;\n")                
#                        rdf_writer.write("\t" + rdfs_ns + "label" + "\t" + '"%s"' % (record['Anatomy']) + " .\n")
#                    else:
#                        rdf_writer.write("\t" + rdf_ns + "type" + "\t" + obo_ns + plant_anatomy_term + " .\n")
#                    ont_hash[anatomy_id] = 1

        if 'TraitID' in record.keys():                        
            if record['TraitID']:
                trait_id = record['TraitID']#.replace(":", "_")
                if ont_pattern.match(trait_id):
                    trait_id = trait_id.replace(":", "_")
                    otl_buffer += "\t" + base_vocab_ns + "has_trait" + "\t" + obo_ns + trait_id + " ;\n"
#                if trait_id not in ont_hash:
#                    rdf_writer.write(obo_ns + trait_id + "\n")
    #                rdf_writer.write("\t" + rdf_ns + "type" + "\t" + obo_ns + plant_trait_term + " ;\n")
#                    if record['TraitName']:
#                        rdf_writer.write("\t" + rdf_ns + "type" + "\t" + obo_ns + plant_trait_term + " ;\n")
#                        rdf_writer.write("\t" + rdfs_ns + "label" + "\t" + '"%s"' % (record['TraitName']) + " .\n")
#                    else:
#                        rdf_writer.write("\t" + rdf_ns + "type" + "\t" + obo_ns + plant_trait_term + " .\n")
#                    ont_hash[trait_id] = 1
            
        otl_buffer = re.sub(' ;$', ' .', otl_buffer)
        rdf_writer.write(otl_buffer)
    rdf_writer.close()
    print "Number of line IDs: %s\n" % (str(germplasm_counter))
    print "*************OryzaTagLine RDF completed***********\n\n"

'''
#otl_test_input = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/oryzaTagLine/OTL_export_pheno+trait.csv'
otl_test_input = '/media/elhassouni/donnees/Noeud-plante-projet/argoLD_project_all_data/data/southgreen/oryzatagline/OTL_export_pheno-trait-csv.tsv'
otl_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/otl_coorection_bug.ttl'

sortie_ds = otlParser(otl_test_input) #otl_test_inputfile otl_inputfile
print(sortie_ds)
otlRDF(sortie_ds, otl_output)#otl_test_output otl_output
        
'''