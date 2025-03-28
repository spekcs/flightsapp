import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuLink,
    NavigationMenuList,
    navigationMenuTriggerStyle,
  } from "@/components/ui/navigation-menu"

import { Link } from "react-router-dom";
import { useAuth } from "@/lib/useAuth";

function Flights() {
    const { isLoggedIn } = useAuth();

    return (
        <div className="flex justify-end pt-2 pr-2">
        <NavigationMenu>
          <NavigationMenuList className="flex space-x-1">
                {!isLoggedIn ? (<>
            <NavigationMenuItem>
              <Link to="/login">
                <NavigationMenuLink className={navigationMenuTriggerStyle()}>
                  Sign in
                </NavigationMenuLink>
              </Link>
            </NavigationMenuItem>
            <NavigationMenuItem>
              <Link to="/register">
                <NavigationMenuLink className={navigationMenuTriggerStyle()}>
                  Sign up
                </NavigationMenuLink>
              </Link>
            </NavigationMenuItem></>) : (<p>logged in</p>)}
          </NavigationMenuList>
        </NavigationMenu>
        </div>
      )
    }

export default Flights;