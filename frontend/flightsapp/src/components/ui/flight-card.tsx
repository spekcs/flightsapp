import {
    Card,
    CardFooter,
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
  const departureTime = flight.departureTime.split('T')[1].substring(0, 5);

  const arrivalTime = flight.arrivalTime.split('T')[1].substring(0, 5);

    return (
        <Card className="hover:bg-accent">
      <div className="flex justify-start gap-1">
      <div className="min-w-48">
  <p className="pl-6 font-bold">{departureTime}</p>
  <p className="pl-6 text-sm text-muted-foreground ">Departure</p>
  <p className="pl-6 text-wrap whitespace-normal">{flight.departureAirport}</p>
    </div>
    <div>
  <p className="pl-6 font-bold">{arrivalTime}</p>
  <p className="pl-6 text-sm text-muted-foreground ">Arrival</p>
  <p className="pl-6">{flight.arrivalAirport}</p>
    </div>
    </div>
  <CardFooter>
        <div className="flex gap-2">
    <p className="font-bold">{flight.date}</p>
    <p className="font-bold text-muted-foreground">{flight.airline}</p>
    <p className="font-bold">{flight.flightTimeMinutes} minutes</p>
        </div>
  </CardFooter>
</Card>

    );
};