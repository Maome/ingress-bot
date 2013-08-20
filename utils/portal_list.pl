#!/usr/bin/perl
# Perl script to read a libpcap file using tshark.  Pull out
# all of the portal locations from a captured Ingress Intel
# session.  Remove duplicates, then sort the portals 
# according to distance from a selected portal
#
# TODO:  Implement a travelling salesman algorithm 
#
# autoflush
$| = 1;
use Getopt::Long;
use Pod::Usage;
use Geo::Distance;

my $geo = new Geo::Distance;
my $quiet = undef;
my $start_portal = undef;
my $max_threshold = undef;
GetOptions( 'help|?' => \$help,
       'man' => \$man,
       'quiet|q' => \$quiet,
       'start=s' => \$start_portal,
       'max|max_distance=i' => \$max_threshold,
) or pod2usage(2);
pod2usage(1) if $help;
pod2usage(-exitstatus => 1, -verbose => 2) if $man;

sub logger($);

$tshark = `which tshark`;
chomp $tshark;

if ( $tshark eq undef )
{
  print "You need to install tshark\n";
  exit;
}

$pcap = $ARGV[0];

if ( ! -f $pcap )
{
  print "Can't read pcap file $pcap\n";
  exit;
}
my @portal_list = ();
read_pcap();

#foreach $pair (@portal_list )
#{
#    print "input pair: $pair\n";
#}

logger "Pulled " . scalar(@portal_list) . " unique portals from the pcap file";

if ( $start_portal eq undef )
{
  $start_portal = $portal_list[0];
}
logger "start portal: $start_portal";
if ( $max_threshold eq undef )
{
  $max_threshold = 20000;
}

my $total_distance = 0;

@final_list = ();

push(@final_list, $start_portal);
next_closest($start_portal);

for ( $i = 0 ; $i < @final_list ; $i++ )
{
  my $pair = $final_list[$i];
  my $prev_pair = $final_list[$i - 1];
  print "$pair\n";
  my $dist = sprintf "%d", find_distance($pair, $prev_pair);
  logger "$pair  (${dist}m)";
}
$total_distance = sprintf "%d", $total_distance;
$rate = sprintf "%.02f", $total_distance / 3600;
logger "Total distance is $total_distance meters, suggest traveling at $rate m/s";

# Find the next closest guy to this pair
sub next_closest()
{
  my $min = 99999999999999999999999999;
  my $closest_pair = undef;
  my $do_next = 0;
  ($pair) = @_;
  my $num_checked = 0;
  foreach $check_pair (@portal_list)
  {
    next if ( $check_pair eq $pair );
    # Skip ones we've already chosen in the @final_list
    $do_next = 0;
    for $checkme (@final_list)
    {
      if ( $checkme eq $check_pair )
      {
        $do_next = 1;
      }
    }
    next if ( $do_next );
    my $dist = find_distance($pair, $check_pair);
    #logger "Checking $pair => $check_pair == $dist";
    next if ( $dist > $max_threshold );
    if ( $dist < $min )
    {
      $min = $dist;
      $closest_pair = $check_pair;
    }
    $num_checked++;
  }
  return if ( $num_checked == 0 );
  $total_distance += $min;
  $min = sprintf "%d", $min;
  logger "closest to $pair is $closest_pair at distance ${min}m";
  push(@final_list, $closest_pair);
  next_closest($closest_pair);
}

sub find_distance()
{
  ($pair1, $pair2) = @_;

  ($lat1,$lon1) = split(/,/, $pair1);
  ($lat2,$lon2) = split(/,/, $pair2);
  my $distance = $geo->distance( 'meter', $lon1,$lat1 => $lon2,$lat2 );
  #print "distance: $distance m\n";
  return $distance
}

sub read_pcap()
{
  logger "Reading pcap file...";
  open(fd, "$tshark -V -r $pcap |");
  my @lines = <fd>;
  close fd;
  chomp @lines;
  my $lon eq undef;
  my $lat eq undef;
  logger "Loading portals...";
  my $do_skip = 0;
  for ( $i = 0 ; $i < @lines ; $i++ )
  {
    my $line = $lines[$i];
    $do_skip = 0;
    #print "line: $line\n";
    if ( $line =~ /Member Key: "lngE6"/ )
    {
      ($lon) = $lines[$i + 1] =~ /Number value:\s*(.+)$/;
    }
    if ( $line =~ /Member Key: "latE6"/ )
    {
      ($lat) = $lines[$i + 1] =~ /Number value:\s*(.+)$/;
    }
    #($lon, $lat) = $line =~ /"lngE6":\s*(.+?),\s*"latE6":\s*(.+)$/;
    next if ( $lat eq undef || $lon eq undef );
    $lon = sprintf  "%0.6f", $lon / 1000000;
    $lat = sprintf "%0.6f", $lat / 1000000;
    next if ( $lat =~ /0\.000000/ || $lon =~ /0\.000000/ );
    #print "$line\n";
    #print "lat: '$lat'  lon: '$lon'\n";
    #print "$lat,$lon\n";
    my $this_portal = "$lat,$lon";
    
    # Check for duplicates...
    foreach $portal (@portal_list)
    {
      if ( $portal eq $this_portal )
      {
        $do_skip = 1;
        last;
      }  
    }
    if ( $do_skip )
    {
      $lon = undef;
      $lat = undef;
      next;
    }
    
    push(@portal_list, $this_portal);
    $lon = undef;
    $lat = undef;
  }
}

sub logger($)
{
  my ($msg) = @_;
  if ( ! $quiet )
  {
    print stderr "$msg\n";
  }
}
__END__

=head1 NAME

portal_list.pl

=head1 SYNOPSIS

portal_list.pl [options] dump.pcap

Perl script to read a libpcap file using tshark.  Pull out
all of the portal locations from a captured Ingress Intel
session.  Remove duplicates, then sort the portals 
according to distance from a selected portal

=head1 OPTIONS

=over 5

=item B<--help>

Show usage information

=item B<--man>

Show the full manual page. 

=item B<--start> "latitude,longitude"

Pick this portal as the start of your journey.  If not specified, the 
first portal in the pcap file is chosen.  

Example:  --start "45.000000,-95.000000"

Hint:  Try to pick something in the middle of town.  Experiment 
with different start locations to try and arrive at the shortest total distance

(TODO:  implement a Travelling Salesman algorithm instead...)

=item B<--max_distance> 20000 | B<--max> 20000

Toss out portals that are this far away, in meters.  Default 20,000 meters, 12.4 miles

=item B<--quiet> | B<-q> 

Don't print interesting info to stderr 


=back

=head1 EXAMPLES

$ sudo tcpdump -i eth0 host www.ingress.com -s 65535 -w portals.pcap 
  # surf around your town on ingress.com/intel, zoom in & out and catch
  # as many portals as you can
  
$ portal_list.pl portals.pcap > locations


=cut

