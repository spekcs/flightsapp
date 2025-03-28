import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
} from "@/components/ui/sidebar"
import { Form, FormItem, FormField, FormLabel, FormControl, FormMessage, FormDescription } from "./form"
import { Input } from "./input"
import { Button } from "./button"
import { z, } from "zod"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { Popover, PopoverTrigger, PopoverContent } from "./popover"

import { format } from "date-fns"
import { CalendarIcon } from "lucide-react"

import { cn } from "@/lib/utils"
import { Calendar } from "@/components/ui/calendar"

const formSchema = z.object({
    departureAirport: z.string().optional(),
    arrivalAirport: z.string().optional(),
    airline: z.string().optional(),
    date: z.date().optional(),
      startHH: z.string().optional(),
      startMM: z.string().optional(),
      endHH: z.string().optional(),
      endMM: z.string().optional(),
});

interface FilterSidebarProps {
    onSubmit: (values: z.infer<typeof formSchema>) => void;
}

export function FilterSidebar({ onSubmit }: FilterSidebarProps) {
    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            departureAirport: "",
            arrivalAirport: "",
            airline: "",
        }
    })


  return (
    <Sidebar collapsible="offcanvas">
      <SidebarContent>
                <SidebarHeader className="text-2xl pt-2">Filter</SidebarHeader>
        <SidebarGroup>
          <SidebarGroupContent className="pt-2">            
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <FormField
              control={form.control}
              name="departureAirport"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Departure Airport</FormLabel>
                  <FormControl>
                    <Input placeholder="Helsinki" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="arrivalAirport"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Arrival Airport</FormLabel>
                  <FormControl>
                    <Input placeholder="Ulemiste" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="airline"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Airline</FormLabel>
                  <FormControl>
                    <Input placeholder="Baltic Air" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

        <FormField
          control={form.control}
          name="date"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Date</FormLabel>
              <Popover>
                <PopoverTrigger asChild>
                  <FormControl>
                    <Button
                      variant={"outline"}
                      className={cn(
                        "w-[240px] pl-3 text-left font-normal",
                        !field.value && "text-muted-foreground"
                      )}
                    >
                      {field.value ? (
                        format(field.value, "PPP")
                      ) : (
                        <span>Pick a date</span>
                      )}
                      <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                    </Button>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="start">
                  <Calendar
                    mode="single"
                    selected={field.value}
                    onSelect={field.onChange}
                    disabled={(date) =>
                      date > new Date() || date < new Date("1900-01-01")
                    }
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
              <FormMessage />
            </FormItem>
          )}/>

          <FormLabel>Departure time range</FormLabel>
          <div className="flex justify-evenly items-center space-x-2">
            <FormField
              control={form.control}
              name="startHH"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <Input maxLength={2} placeholder="HH" {...field} />
                  </FormControl>
                </FormItem>
              )}
            />
            <span className="self-center">:</span>
            <FormField
              control={form.control}
              name="startMM"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <Input maxLength={2} placeholder="MM" {...field} />
                  </FormControl>
                </FormItem>
              )}
            />
            <p className="self-center">-</p>
            <FormField
              control={form.control}
              name="endHH"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <Input maxLength={2} placeholder="HH" {...field} />
                  </FormControl>
                </FormItem>
              )}
            />
            <span className="self-center">:</span>
            <FormField
              control={form.control}
              name="endMM"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <Input maxLength={2} placeholder="MM" {...field} />
                  </FormControl>
                </FormItem>
              )}
            />
          </div>
            <Button type="submit">Apply</Button>
          </form>
        </Form>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  )
}
