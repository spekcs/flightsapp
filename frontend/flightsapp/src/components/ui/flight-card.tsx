import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
  } from "@/components/ui/card"
  
type FlightCardProps = {
    flight: {
        flightId: number,
        departureAirport: string,
        arrivalAirport: string,
        date: string,
        departureTime: string,
        arrivalTime: string,
        flightTimeMinutes: number,
        airline: string
    }
}

export function FlightCard( {flight}: FlightCardProps) {
    return (
        <Card className="hover:bg-accent">
  <CardHeader>
    <CardTitle>Card Title</CardTitle>
    <CardDescription>Card Description</CardDescription>
  </CardHeader>
  <CardContent>
    <p>{flight.departureAirport}</p>
  </CardContent>
  <CardFooter>
    <p>Card Footer</p>
  </CardFooter>
</Card>

    );
};