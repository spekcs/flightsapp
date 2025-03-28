import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuList,
    navigationMenuTriggerStyle,
  } from "@/components/ui/navigation-menu"

import { FilterSidebar } from "@/components/ui/filter-sidebar";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";

import { Link } from "react-router-dom";
import { useAuth } from "@/lib/useAuth";
import { Button } from "@/components/ui/button";
import { useEffect, useState } from "react";

function Flights() {
    const { isLoggedIn: initialIsLoggedIn } = useAuth();
    const [isLoggedIn, setIsLoggedIn] = useState(initialIsLoggedIn);
  
    const handleSignout = () => {
      localStorage.removeItem("jwt")
      setIsLoggedIn(false);
    }
  
    useEffect(() => {
      const authStatus = localStorage.getItem("jwt")
      setIsLoggedIn(!!authStatus)
    }, []);
  
    const handleApplyFilters = (values) => {
      console.log(values)
    }

    return (<>
        <div className="flex justify-end pt-2 pr-2">
        <NavigationMenu>
          <NavigationMenuList className="flex space-x-1">
                {!isLoggedIn ? (<>
            <NavigationMenuItem>
              <Link to="/login" className={navigationMenuTriggerStyle()}>
                  Sign in
              </Link>
            </NavigationMenuItem>
            <NavigationMenuItem>
              <Link to="/register" className={navigationMenuTriggerStyle()}>
                  Sign up
              </Link>
            </NavigationMenuItem></>) : (<>
              <NavigationMenuItem>
                <Button onClick={handleSignout} variant="link">Sign out</Button>
              </NavigationMenuItem>
              <NavigationMenuItem>
              <Link to="/profile" className={navigationMenuTriggerStyle()}>
                  Profile
              </Link>
              </NavigationMenuItem></>
            )}
          </NavigationMenuList>
        </NavigationMenu>
        </div>
        <SidebarProvider>
        <FilterSidebar onSubmit={handleApplyFilters} />
            <SidebarTrigger/>
      </SidebarProvider>
      </>
      )
    }

export default Flights;