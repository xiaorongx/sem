=begin
A parser that reads from files in
ftp://ftp.ncbi.nlm.nih.gov/snp/organisms/human_9606/XML/
The parser uses a SAX XML parser called nokogiri

To use: ruby parse_snp.rb inputfile
The result writes to inputfile+'.out'
=end

require 'rexml/document'
require 'rubygems'
require 'nokogiri'
require 'open-uri'

class MyDoc < Nokogiri::XML::SAX::Document
  attr_accessor :inputfile  # constructor arguments define setter and getter methods behind the scene
  def initialize(inputfile)
    @inputfile = inputfile
  end

  #variables to remember some internal states while parsing the XML
  @rsid = nil
  @frequency = nil
  @consequences = nil
  @reference_assembly = nil
  @group_term = nil
  @processFxnSet = nil

  def start_element name, attrs = []
    if name == 'Rs' then
      reset()
      # puts "starting: #{name} #{attrs}"
      rsid = attrs.select { |attr, value| attr == 'rsId' }.first()
      @rsid = rsid[1]
      # puts "rsid: #{@rsid}"
    end
    if name == 'Frequency' then
      frequency = attrs.select { |attr, value| attr == 'freq' }.first()
      @frequency = frequency[1]
      # puts "frequency: #{@frequency}"
    end
    if name == 'Assembly' then
      reference_assembly = attrs.select { |attr, value| attr == 'reference' }.first()
      @reference_assembly = reference_assembly[1]
      # puts "reference: #{@reference_assembly}"
    end
    if name == 'Component' and @reference_assembly == "true" then
      # puts "starting: #{name} #{attrs}"
      group_term = attrs.select { |attr, value| attr == 'groupTerm' }.first()
      @group_term = (group_term) ? group_term[1] : nil
      # puts "groupTerm: #{@group_term}"
    end
    if name == 'MapLoc' and @group_term and @group_term.start_with?("NC") then
      # puts "starting: #{name} #{attrs}"
      @processFxnSet = 'true'
    end
    if name == 'FxnSet' and @processFxnSet == 'true' then
      puts "starting: #{name} #{attrs}"
      geneid = attrs.select { |attr, value| attr == 'geneId' }.first()[1]
      symbol = attrs.select { |attr, value| attr == 'symbol' }.first()[1]
      mrna_acc = attrs.select { |attr, value| attr == 'mrnaAcc' }.first()[1]
      residue = attrs.select { |attr, value| attr == 'residue' }.first()
      residue = (residue) ? residue[1] : nil

      aa_pos = attrs.select { |attr, value| attr == 'aaPosition' }.first()
      aa_pos = (aa_pos) ? aa_pos[1] : nil

      fxn_class = attrs.select { |attr, value| attr == 'fxnClass' }.first()
      fxn_class = (fxn_class) ? fxn_class[1] : nil

      if symbol and mrna_acc and residue and aa_pos and fxn_class then
        puts [geneid, symbol, mrna_acc, residue, aa_pos, fxn_class].join("|")
        # puts @rsid
        id = [@rsid, symbol, mrna_acc, aa_pos]
        @consequences[id] = ["unknown", "unknown", "unknown"] unless @consequences[id]
        before_after = (fxn_class == "reference") ? 0 : 1
        @consequences[id][before_after] = residue
        @consequences[id][2] = fxn_class unless fxn_class == "reference"
      end
    end
  end

  def end_element name
    if name == 'Assembly' then
      @reference_assembly = nil
    end
    if name == 'Component' then
      @group_term = nil
    end
    if name == 'MapLoc' then
      @processFxnSet = nil
    end
    if name == 'Rs' && @consequences then
      # puts "ending: #{name}"
      list = []
      @consequences.each { |k, v|
        rsid, symbol, mrna_acc, aa_pos = k
        before, after, fxn = v
        next if fxn=='synonymous-codon'
        frequency = "unknown" unless @frequency
        puts "rsid: #{rsid}, frequency: #{frequency}, before: #{before}, aa_pos: #{aa_pos}, after: #{after}, fxn: #{fxn}"

        # puts '> rs'+rsid+"\t"+frequency+"\t"+symbol+":"+before+(aa_pos.to_i+1).to_s + after + '=' + fxn
        list << rsid+"\t"+symbol+":"+before+(aa_pos.to_i+1).to_s + after + '=' + fxn

      }
      list = list.sort.uniq
      per_snp_hash = Hash.new
      list.each { |row|
        rsid, effect = row.split("\t")
        rsid = 'rs'+rsid.to_s
        if (per_snp_hash[rsid]) then
          per_snp_hash[rsid] += '+' + effect
        else
          per_snp_hash[rsid] = effect
        end
      }
      list = per_snp_hash.to_a
      append_file(list, inputfile+'.out')
    end
  end

  def append_file(list, filename)
    outfile = File.open(filename, 'a+')
    list.each { |row| outfile.puts row.join("\t") }
    outfile.close
  end

  def reset()
    @rsid = nil
    @frequency = nil
    @consequences = Hash.new
    @reference_assembly = nil
    @group_term = nil
    @processFxnSet = nil
  end

end

inputfile = ARGV[0]
parser = Nokogiri::XML::SAX::Parser.new(MyDoc.new(inputfile))
parser.parse(File.open(inputfile))


