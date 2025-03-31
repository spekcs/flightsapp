import {
    Sidebar,
    SidebarContent,
    SidebarGroup,
    SidebarGroupContent,
    SidebarHeader,
  } from "@/components/ui/sidebar"
import { Input } from "./input"
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectLabel,
    SelectTrigger,
    SelectValue,
  } from "@/components/ui/select"
import { Button } from "./button"


interface SeatsSidebarProps {
    flight: {
        flightId: number,
        departureAirport: string,
        arrivalAirport: string,
        date: string,
        departureTime: string,
        arrivalTime: string,
        flightTimeMinutes: number,
        airline: string
    } | undefined,
    error: string,
    count: number,
    setCount: any,
    bookedSeatCount: number,
    setRecommendBy: any,
    selectedSeats: string[],
    onSubmit: any
}


export function SeatsSidebar({ flight, error, count, setCount, bookedSeatCount, setRecommendBy, onSubmit, selectedSeats }: SeatsSidebarProps) {
const departureTime = flight?.departureTime.split('T')[1].substring(0, 5);
const arrivalTime = flight?.arrivalTime.split('T')[1].substring(0, 5);

const onCountChange = (e: any) => {
    setCount(e.target.value)
    console.log(e.target.value)
}

const onRecommendByChange = (value: any) => {
    setRecommendBy(value)
    console.log(value)
}

return (
    <Sidebar collapsible="offcanvas">
    <SidebarContent>
        <SidebarHeader className="text-2xl pt-2">Select seats</SidebarHeader>
        <SidebarGroup className="gap-y-2">
        <SidebarGroupContent className="pt-2 pb-2">            
            {error && <p className="text-red-500">{error}</p>}
            {flight  &&  
            (<div>
                <div className="flex">
                    {departureTime} {flight.departureAirport}
                </div>
                <div className="flex">
                    {arrivalTime} {flight.arrivalAirport}
                </div>
                <p>{flight.airline}</p>
                <p>{flight.date}</p>
            </div>)}
        </SidebarGroupContent>
        <SidebarGroupContent>
            <p>Number of seats</p>
            <Input type="number" onChange={onCountChange} value={count} max={132 - bookedSeatCount} min={1}></Input>
        </SidebarGroupContent>
        <SidebarGroupContent className="pb-2">
            <p>Recommend by</p>
        <Select onValueChange={onRecommendByChange} defaultValue="TOGETHER">
            <SelectTrigger className="w-[180px]">
            <SelectValue placeholder="Seats together"/>
            </SelectTrigger>
            <SelectContent>
                <SelectGroup>
                    <SelectLabel>Recommend by</SelectLabel>
                    <SelectItem value="TOGETHER">Seats together</SelectItem>
                    <SelectItem value="WINDOW">Window seats</SelectItem>
                    <SelectItem value="LEGROOM">More legroom</SelectItem>
                    <SelectItem value="EXIT">Near exit</SelectItem>
                </SelectGroup>
            </SelectContent>
        </Select>
    </SidebarGroupContent>
    <SidebarContent className="flex justify-center">
        <p className="font-bold text-2xl">Your seats:</p>
        <p className="font-bold text-xl">
        {selectedSeats?.map((seat) => (`${seat} `))}
        </p>
        <Button onClick={onSubmit} className="max-w-18">Book</Button>
    </SidebarContent>
    </SidebarGroup>
    </SidebarContent>
    </Sidebar>
)
}
