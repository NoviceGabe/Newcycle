-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 14, 2020 at 05:49 PM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id14312373_newcycle`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `last_login` varchar(255) NOT NULL,
  `IP_address` varchar(255) NOT NULL,
  `is_now_login` enum('FAIL','SUCCESS') NOT NULL DEFAULT 'FAIL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE `order` (
  `id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `code` varchar(255) NOT NULL,
  `payment_method` enum('Credit Card','E Wallet','COD','') NOT NULL DEFAULT 'Credit Card',
  `order_status` enum('PENDING','AWAITING_PAYMENT','AWAITING_SHIPMENT','AWAITING_PICKUP','PARTIALLY_SHIPPED','SHIPPED','COMPLETED','CANCELLED','DECLINED','PARTIALLY_REFUNDED','REFUNDED','DISPUTED','VERIFICATION_REQUIRED') NOT NULL DEFAULT 'PENDING',
  `date_ordered` varchar(255) NOT NULL,
  `date_shipped` varchar(255) DEFAULT NULL,
  `date_received` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` int(11) NOT NULL,
  `shipping_fee` int(11) NOT NULL,
  `description` text NOT NULL,
  `stock` int(11) NOT NULL DEFAULT 0,
  `image` text DEFAULT NULL,
  `rating` int(11) NOT NULL DEFAULT 0,
  `type` enum('1','2','3','') NOT NULL,
  `date` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `name`, `price`, `shipping_fee`, `description`, `stock`, `image`, `rating`, `type`, `date`) VALUES
(41, 'RASCAL – GX EAGLE KIT', 8999, 150, 'Frame Size	\r\nSM- $5,199, MD- $5,199, LG- $5,199, XL- $5,199\r\n\r\nFrame Color	\r\nT1000, Sedona\r\n\r\nShock Choice	\r\nRockShox Super Deluxe Select-Stock+$0, RockShox Super Deluxe Ultimate +$100, Fox Float X2 Factory +$400, Push ELEVENSIX +$950\r\n\r\nFork Choice	\r\nRockShox Pike Select 140mm-Stock +$0, RockShox Pike Ultimate RCT3 140mm Black +$250, RockShox Pike Ultimate RCT3 140mm Silver +$250, Fox Float 36 Factory 140mm +$400\r\n\r\nRider Weight	\r\nUnder 100 lb, 105 lb, 110 lb, 120 lb, 125 lb, 130 lb, 135 lb, 140 lb, 145 lb, 150 lb, 155 lb, 160 lb, 165 lb, 170 lb, 175 lb, 180 lb, 185 lb, 190 lb, 195 lb, 200 lb, 205 lb, 210 lb, 215 lb, 220 lb, 225 lb, 230 lb, 235 lb, 240 lb, 245 lb, 250 lb, 255 lb, 260 lb, Over 260 lb\r\n\r\nWheelset Choice	\r\nIndustry 9 1/1-Stock +$0, RW30 Industry 9 1/1- +$999, RW30 Industry 9 Hydra- +$1199\r\n\r\nDropper Length	\r\n150mm, 170mm\r\n\r\nCrank Length	\r\n170mm', 10, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_Revel_PNG_01.png', 0, '1', '1602504419'),
(42, 'Vale Go 9D EQ', 20809, 200, 'The Vale Go 9D EQ will comfortably handle anything from everyday errands to long commutes to fun getaways. This e-bike features a powerful Bosch Active Plus motor and fully integrated battery into a step-thru fame. Bosch Kiox display delivers Bluetooth connectivity and advanced rider information. Most importantly its an Electra, so its a blast to ride.\r\n\r\nFrame\r\n6061-T6 Aluminum with patented Flat Foot Technology\r\n\r\nFork\r\nAlloy Hydroformed\r\n\r\nFront Hub\r\nAlloy low-flange with 6-bolt disc, 32h, 12 mm thru-axle\r\n\r\nRear Hub\r\nAlloy low-flange with 6-bolt disc, 32h, 12 mm thru-axle\r\n\r\nRims\r\nAlloy double-walled 27.5 inches x 32 h\r\n\r\nTires\r\nSchwalbe Super Moto-X 27.5 inches x 2.4 inches with puncture resistant Kevlar guard casing and Reflective Stripe\r\n\r\nShifters\r\nShimano Acera Rapid-Fire Plus\r\n\r\nFront Derailleur\r\nNone\r\n\r\nRear Derailleur\r\nShimano Alivio 9-speed\r\n\r\nCrank\r\nFSA forged alloy 170 mm, Bosch type, 40t FSA Chain Ring\r\n\r\nBottom Bracket\r\nNone\r\n\r\nCassette\r\nShimano 9-speed 11-36t\r\n\r\nChain\r\nKMC E9T nickel plated\r\n\r\nPedals\r\nResin Platform with Grip Tape Tread\r\n\r\nSaddle\r\nSelle Royal Essenza\r\n\r\nSeatpost\r\nAlloy Suspension post 27.2 x 400 mm, 40 travel\r\n\r\nHandlebar\r\nAlloy Custom Bend\r\n\r\nGrips\r\nElectra Vale custom ergo Kraton with Alloy Locking collars\r\n\r\nStem\r\nForged Alloy 31.8 mm threadless, 60 mm extension, Blendr compatible\r\n\r\nHeadset\r\nFSA e2 integrated\r\n\r\nBrakeset\r\nFront or Rear Tektro Hydraulic disc brakes\r\n\r\nBattery\r\nBosch Powertube 500, includes 4 AMP Charger\r\n\r\nMotor\r\nBosch Active Line Pls, 250 W\r\n\r\nFront Light\r\nSpannings LED\r\n\r\nRear Light\r\nSpannings LED\r\n\r\nWeight\r\n24.45 kg or 54 lbs', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_Vale Go 9D EQ 2020.jpg', 0, '1', '1602604643'),
(43, 'Shimano 105 5800 2 x 11 Speed STI Shifter', 6799, 100, 'With shifter cables, without brake cables and housing\r\nSLR stands for Shimano Linear Response. The Super SLR design makes use of a friction-reducing mechanism in the caliper, lever and cable that improve response and modulation.\r\n\r\nFeatures\r\n\r\n-Comfortable lever design for better rider comfort and control\r\n-Slim and compact bracket with smooth surface with enclosed mechanism\r\n-Screw type Reach Adjust (10mm)\r\n-Light action, defined click engagement\r\n-Short lever stroke response\r\n-Polymer coated cables to reduce friction\r\n-Powerful and controllable braking system\r\n-Weight 486g (pair)\r\n', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_STI_ULT_6800_1024x1024.webp', 0, '3', '1602606524'),
(44, 'Shimano Deore SL-M610 10 Speed Shifter ISPEC-B', 1249, 100, 'Shimano Deore M610 10 Speed Trigger Shifter Set\r\nDeore mountain bike Rapid-fire shifter pods featuring 2-way release for cost effective Dyna-Sys 10-speed shifting performance.\r\n\r\nFeatures\r\n\r\n-Light to the touch but positive and crisp, Vivid indexing provides a constant amount of shift lever force across all 10 sprockets\r\n-2-way release allows shifting from various riding positions the upper lever still performs the same shift function, but it can now swing in either \r\ndirection allowing it to be released by the thumb or index finger\r\n-Dyna-Sys10-speed compatible - for use with precision 10-speed MTB drivetrains\r\n-Pod design allows the use of these shifters with any brake lever\r\n-2nd generation I-spec-B design is developed to attach directly to I-spec-B brake levers\r\n-Mode converter for double/triple use\r\n-Barrel adjuster for fine gear tuning\r\n-Includes inner cables but not outer cable housing\r\n-Easy access clamp band adjustment for quick set up\r\n-Weight 265g\r\n', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_SLX Shifter ISPEC-B.webp', 0, '3', '1602606822'),
(45, 'Shimano Dura Ace SL-BSR1 11 Speed Bar End Shifter Set', 3199, 100, 'Precision-engineered Dura-Ace 11-speed bar end shift levers\r\n\r\nFeatures\r\n\r\n-Superior Dura-Ace quality means reliability and durability\r\n-Easily fit into bar ends and secure firmly for assured shifting\r\n-Fully compatible with 11-speed drivetrains\r\n-SIS indexed system compatible\r\n-Comes complete with SP41 gear cables, which are lubricated internally along their length with low-friction silicon grease to reduce cable friction.\r\n-Comes with shifter cables\r\n', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_Speed Bar End Shifter Set.webp', 0, '3', '1602606924'),
(46, 'Shimano Tiagra 4700 2x10 Speed STI Shifter', 4670, 100, 'STI shift-brake levers compatible to 2x10-speed Shimano Tiagra 4700 components.  The Tiagra Dual Control levers feature a reach adjustable ergonomic design.\r\n\r\nFeatures\r\n\r\n-Suitable for handlebars with a diameter of 23.8-24.2mm\r\n-Includes shifter cables\r\n-Does not include brake cables and housing\r\n', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_TIAGRA_STI_4700_1024x1024.webp', 0, '3', '1602607186'),
(47, 'Shimano 105 5800 Clipless Road Pedals', 3899, 100, 'SPD-SL road pedal for high performance road racing. Designed to match the 105 11-speed group.\r\n\r\nFeatures\r\n-Wide lightweight carbon body provides large shoe contact area to maximize power transfer and support whilst reducing weight\r\n-Low profile design increases road clearance for increased confidence when slicing through corners\r\n-Large binding target allows quick engagement while wide cleats provides more efficient pedaling\r\n-Stainless steel pedal body plate for increased durability\r\n-Open design allows for easy access and cleat adjustment\r\n-Low maintenance sealed cartridge axle unit\r\n-Three types of shoe cleats available fixed, 3 degrees of float in each direction, or 3 degrees of float from a font-center pivot\r\n-Weight 285g per pair\r\n', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_ULTEGRA_1024x1024.webp', 0, '3', '1602607475'),
(48, 'Shimano 105 5800 2 x 11 Speed STI Shifter', 6799, 100, 'Ideal pedal for recreational riders who commute and tour. Combines SPD mechanism on one side and flat pedal body on the other, you can ride with cleats or normal shoes.\r\n\r\nFeatures\r\n-Serviceable cup and cone bearings add to long life of the pedals\r\n-Adjustable cleat tension means you can start off with loose tension for extra easy engagement and release, and turn up the tension as you progress\r\n-A sealed mechanism and serviceable cup and cone bearings', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_M324_1024x1024.jpg', 0, '3', '1602608057'),
(49, 'Shimano M424 Clipless MTB Pedals 	', 1399, 100, 'Amazing low priced SPD pedal for all round use including tough Freeride and off-road use. Lightweight and supportive pedal with high impact resin cage for greater foot stability.\r\n\r\nFeatures\r\nDual-sided pop-up bindings that position the body at 12.5 degrees angle for easy and fast entry\r\nLarge cages for non-cleated shoes for all around use\r\nLow maintenance sealed bearing cartridge axle\r\nCleat tension adjustment for each rider preference\r\n', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_M424_1024x1024.jpg', 0, '3', '1602608115'),
(50, 'Shimano M505 Clipless MTB Pedals', 999, 100, 'Most basic Shimano mountain bike clipless pedal\r\ncomes with SH-51 cleats', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_M505_1024x1024.png', 0, '3', '1602608174'),
(51, 'Shimano M530 Trail Clipless MTB Pedals', 1299, 100, 'Value for money dual-sided SPD pedal, perfect for cross country trail riding this trail pedal is ideally suited to tackle single track and ultra-technical descents with control and confidence.\r\n\r\nFeatures\r\n-Integrated cage increases stability and control when not clipped in and protects binding mechanism against impacts\r\n-Sealed cartridge bearing spindle keeps out water and mud for a smooth action, and makes for a durable pedal\r\n-Better mud and debris shedding than any pedal in its class\r\n-Adjustable entry and release tension settings\r\n-Pedal Surface Dual Sided\r\n-Cleat SM-SH51 Cleat\r\n-Weight 455g per pair\r\n', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_M530_1024x1024.webp', 0, '3', '1602608271'),
(52, 'Shimano Deore M615 Disc Hub Set Center Lock 36 Holes', 1200, 100, 'Shimano have been producing bike components since 1921. Over the years they have designed and developed some of the best MTB hubs and spares ever seen.\r\n\r\nFeatures\r\n-Super polished bearing races for low friction rolling\r\n-Light alloy QR lever and nut\r\n-Hub/Brake Compatibility Center-lock Disc\r\n-Rear Axle Type 10x1\r\n-Color Black or Silver\r\n-Cassette Body Type Shimano 9-10-11\r\n-Rear Hub Spacing 135mm\r\n-Intended Use Mountain\r\n-Rear Wheel Type Mountain\r\n-Skewer Included Yes\r\n-Weight 512.0g', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_DEORE_615_FRONT_1024x1024.webp', 0, '3', '1602608465'),
(53, 'Step-Thru Townie Go 7D 2020', 10809, 200, 'Finally, an e-bike for the rest of us. The Townie Go 7D combines the comfort and control of the best-selling bike in the U.S. with the power and fun of an e-bike. With 3 levels of support, 26inches tires and Electras patented Flat Foot Technology, you can go faster, farther, and funner for less than you thought possible.\r\n\r\nFrame\r\n6061-T6 aluminum with patented Flat Foot Technology\r\n\r\nFork\r\nHi-ten steel unicrown, straight or tapered leg\r\n\r\nFront hub\r\nAlloy low-flange with 6-bolt disc, 36h\r\n\r\nRear hub\r\nAlloy low-flange with 6-bolt disc, 36h\r\n\r\nRims\r\nAnodized alloy 26inches x 36h\r\n\r\nTires\r\n26inches x 2.35inches balloon\r\n\r\nShifters\r\nShimano Revo 7-speed twist with optical gear display\r\n\r\nFront derailleur\r\nNone\r\n\r\nRear derailleur\r\nShimano Tourney TY 7-speed\r\n\r\nCrank\r\nForged alloy 170 mm, 42t FSA chainring with double chainguards\r\n\r\nBottom bracket\r\nHydrive t47 x 155 mm with integrated torque sensor\r\n\r\nCassette\r\nShimano 7-speed 14-34t\r\n\r\nChain\r\nKMC Z7\r\n\r\nPedals\r\nResin platform with non-slip rubber tread\r\n\r\nSaddle\r\nErgonomic with shock-absorbing elastomers\r\n\r\nSeatpost\r\nAlloy straight post 27.2 x 350 mm\r\n\r\nHandlebar\r\nAlloy custom bend 25.9inches width or 4inches rise\r\n\r\nGrips\r\nComfort Kraton\r\n\r\nStem\r\nForged alloy 25.4 mm quill, 80 mm extension\r\n\r\nHeadset\r\n1 1 or 8inches steel threaded or semi-integrated\r\n\r\nBrakeset\r\nFront or rear  mechanical disc brakes\r\n\r\nBattery\r\nHydrive 309Wh, rear rack mounted, includes 2 AMP charger\r\n\r\nMotor\r\nHydrive rear hub motor, 250w\r\n\r\nWeight\r\n21.18 kg  or  48 lbs\r\n', 2, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_Step-Thru Townie Go 7D 2020.jpg', 0, '1', '1602608482'),
(54, 'Cantilever', 2800, 50, 'A cantilever is a rigid structural element that extends horizontally and is supported at only one end. Typically it extends from a flat vertical surface such as a wall, to which it must be firmly attached. Like other structural elements, a cantilever can be formed as a beam, plate, truss, or slab.', 3, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_Cantilever.webp', 0, '2', '1602608529'),
(55, 'Shimano 105 5800 SS 11 Speed Road Rear Derailleur', 1120, 100, 'High performance 11-speed 105 wide link rear derailleur\r\n\r\nFeatures\r\n-Advanced Wide Link design increases rigidity and shifting performance\r\n-Super stiff for fast and accurate changes across the cassette\r\n-Large 11 tooth pulleys increase service life and reduce noise\r\n-Super light shift action\r\n-Compatible with 11-speed drivetrains\r\n-Maximum low sprocket 28T with double chain set\r\n-Maximum front difference 16T, total capacity 28T\r\n-Average weight 232 grams', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_105_5800_RD_1024x1024.webp', 0, '3', '1602608603'),
(56, 'Shimano Alivio M4000 9 Speed Front Derailleur', 485, 100, 'Alivio has been used on trekking bikes for many years and now Shimano have released a dedicated trekking group set. The new Alivio Trekking components are now slimmer, quieter and offer a smoother ride than ever before. The first thing that stands out at the Alivio mountain bike group set is the new design. Compared to the previous group set it has a more aggressive look and a low profile design. The new front derailleur gives more tire clearance for bigger wheel sizes and improves the maneuverability.\r\n\r\nFeatures\r\n-MEGA-9 LITE drivetrain\r\n-Best design for big wheel\r\n-Wide link for longer durability and precise shifting\r\n-Light operation force for stress free long riding\r\n-Quick and fast assemble feature\r\n-Use MTB\r\n-Maximum Capacity Total capacity 18 T\r\n-Top Gear Teeth 40 T\r\n-Cable Routing Cable route type Dual-Pull\r\n-Chain Line 50mm\r\n-Chain Stay Angle 66-69 degree', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_ALIVIO_FD_M4000_HIGH_1024x1024 (1).webp', 0, '3', '1602608809'),
(57, 'Shimano Alivio M4000 9 Speed MTB Rear Derailleur', 900, 100, 'Alivio MTB - dedicated Mountain Biking components. Control with Confidence Proven race technology with improved ergonomics, durability and reliability.\r\nShadow Rear derailleur is intended for more aggressive riding. Because of its low profile and single tension construction, the derailleur does not hit the chain stay in rough riding conditions. The result is smooth and silent performance.\r\n\r\nFeatures\r\n-Super low-profile concept with SHADOW RD technology for less trouble\r\n-Quick wheel installation and removal\r\n-Mount Type Direct attachment (Conventional)\r\n-Maximum Sprocket Low sprocket Max. 36T Top sprocket Max. 12T\r\n-Minimum Sprocket Low sprocket Min. 32T Top sprocket Min. 11T\r\n-Front Difference Max. front difference 22T\r\n-Total Capacity 45T', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_ALIVIO_RD_M4000_1024x1024.webp', 0, '3', '1602609099'),
(58, 'Comfort Saddle', 1000, 50, 'Comfort saddles can be used for long distance touring and are often designed to absorb some of the shock and vibrations from rough country roads. Women-specific saddles might come into this category as they also have a wider seat to suit the female anatomy, a shorter nose and center relief.', 1, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_Comfort-Saddles.png', 0, '2', '1602609102'),
(59, 'Shimano 105 R7000 11 Speed Road Bike Group set', 20500, 100, 'The new Shimano R7000 group set, the latest version of the ever popular 105.Featuring trickle down technology from the Dura Ace R9100 and Ultegra R8000 giving you all the features at a very attractive price point.\r\n \r\nGroup set Specification\r\n-Crankset FC-R7000 choice of crank length and gear ratio\r\n-Cassette CS-R7000 Choice of Ratios (11-28, 11-30 and 11-32)\r\n-Chain CN-HG701\r\n-Front Derailleur FD-R7000 (braze on)\r\n-Rear Derailleur RD-R7000 SS \r\n-Brake/Gear levers ST-R7000 STI including all cables (no cable housing included)\r\n-Brake set BR-R7000', 5, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_Shimano 105 R7000 11 Speed Road Bike Groupset.jpg', 0, '3', '1602609323'),
(60, 'Michelin Protek Urban', 1110, 50, 'Description of Michelin Protek Urban Reflex Competition Line Wired Tire - 37-622. The urban tire for maximum safety. Exceptional grip on wet surfaces and unequaled ease of rolling thanks to an extremely easy-rolling tread pattern and a compound derived from MICHELIN PRO4 Grip road tires.', 1, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_Michelin Protek Urban.jpg', 0, '2', '1602609486'),
(61, 'Mountain Bike Frame', 11101, 50, 'Mountain bike geometry will often feature a seat tube angle\r\naround 73 degrees, with a head tube angle of anywhere from\r\n60-73 degrees. The intended application of the bike affects its\r\ngeometry very heavily. In general, steeper angles (closer to 90\r\ndegrees from the horizontal) are more efficient for pedaling up\r\nhills and make for sharper handling. Slacker angles (leaning\r\nfarther from the vertical) are preferred for high speeds and\r\ndownhill stability.', 1, '', 0, '2', '1602610000'),
(62, 'Townie Go 8D Step-Over 2019', 15809, 200, 'Just get on and Go The Electra Townie Go 8D with a Bosch Active System motor and Flat Foot Technology frame geometry make this e-bike the perfect blend of comfort, control and fun. With up to a 100-mile range, you can go to work, go to the store, go wherever you want without breaking a sweat. This pedal-assist e-bike is easy to use and a blast to ride.\r\nFrame\r\n6061-T6 aluminum with patented Flat Foot Technology\r\nFork\r\nHi-ten steel uni-crown, straight or tapered leg\r\nFront hub\r\nAlloy low-flange disc 36h\r\nRear hub\r\nAlloy low-flange disc 36h\r\nRims\r\nAnodized alloy 26inches x 36h\r\nTires\r\n26x2.35inches balloon\r\nShifters\r\nShimano Acera rapid fire plus\r\nRear derailleur\r\nShimano Tourney 8-speed\r\nCrank\r\nFSA forged alloy 170mm, Bosch type, 38T FSA chainring\r\nCassette\r\nSram PG-820 11-32 8-speed\r\nChain\r\nKMC X 8.99 nickel plated\r\nPedals\r\nAlloy platform withnon-slip rubber tread\r\nSaddle\r\nErgonomic withshock-absorbing elastomers\r\nSeatpost\r\nAlloy straight post 27.2 x 400 mm\r\nHandlebar\r\nAlloy custom bend 25.9inches width or 4inches rise\r\nGrips\r\nElectra hand-stitched leatherette\r\nStem\r\nForged alloy 25.4mm quill, 80mm extension\r\nHeadset\r\n1 1 or 8inches steel threaded or semi-integrated\r\nBrakeset\r\nFront or rear Tektro Mechanical disc brakes\r\nBattery\r\nBosch PowerPack 400, includes 2 AMP charger\r\nController\r\nBosch Purion computer display unit\r\nMotor\r\nBosch Active Line 250 watts\r\nExtras\r\nBosch Purion computer display unit, reflective battery decal, ring lock mounts, internal cable routing, alloy single leg kickstand, and stainless steel and anti-rust hardware\r\nWeight\r\n21.18 kg or  48 lbs\r\n', 3, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_Townie Go! 8D Step-Over 2019.jpg', 0, '1', '1602698601'),
(63, 'CANNONDALE STREET 24 2018', 19799, 200, 'Tear up the tarmac with this lightweight, high-performance machine. With its 21-speed gearing, all-weather disc brakes and radical Lefty-inspired single sided fork, the Street 24 is a street bike like no other.\r\nFRAME\r\nStreet 24, Optimized 6061 alloy, smooth welding \r\n\r\nFORK\r\nCannondale Lefty Rigid, 24, 1.125 inches steerer\r\nRIMS\r\nAlex ACE20, double wall, 32 hole\r\nHUBS\r\nKT Alloy, 7 speed.\r\nSPOKES\r\nSteel, 14g Black\r\nTIRES\r\nCannondale 24x1.5 inches, wire bead\r\nPEDALS\r\nVP Composite platform with reflectors\r\nCRANK\r\nSuntour Alloy 152mm crank, 24 or 34 or 42T steel\r\nBOTTOM BRACKET\r\nCartridge square taper\r\nCHAIN\r\nKMC Z50, 0.5 x 0.09375 inches\r\nREAR COGS\r\nShimano, 7-speed freewheel, 14-28\r\nFRONT DERAILLEUR\r\nShimano, TX-35, 31.8 band\r\nREAR DERAILLEUR\r\nShimano, TX-35, 7 Speed, MegaRange\r\nSHIFTERS\r\nShimano, RS-36, Revo Twist\r\nHANDLEBAR\r\nAlloy, 580mm wide, 25.4mm\r\nGRIPS\r\nCannondale Kid Performance\r\nSTEM\r\nAlloy Ahead, 60mm\r\nHEADSET\r\nTange Seiki Alloy, 1.125 inches Ahead, integrated\r\nBRAKES\r\nTektro, Novela Mechanical disc brakes\r\nBRAKE LEVERS\r\nTektro Alloy with barrel adjuster\r\nSADDLE\r\nCannondale Boy Ergo\r\nSEATPOST\r\nCannondale Alloy, 27.2\r\nSIZES\r\nOne Size\r\n', 3, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_bicycle_kid_bike_01.png', 0, '1', '1602699488'),
(64, 'CANNONDALE TRAIL 16 KID BIKE 2019', 11399, 200, 'With one-speed simplicity and an easy-to-use kick-back style coaster brake, this lightweight ripper is perfect for building skills and having fun.\r\nFRAME\r\nTrail 16 SS, SmartForm C2 Alloy, smooth welding\r\nFORK\r\nTrail 16 Rigid, steel, 1 inches steerer\r\nRIMS\r\nCannondale C4, lightweight alloy, 16 inches, 28h\r\nHUBS\r\nAlloy, nutted axles, rear coaster brake\r\nSPOKES\r\nSteel, 14g Black\r\nTIRES\r\nInnova, 16 x 1.75 inches, multi-use tread\r\nPEDALS\r\nWellgo Composite Platform with reflectors, 0.5 inches\r\nCRANK\r\nOne-piece, 110mm length, 28T\r\nBOTTOM BRACKET\r\nIncluded\r\nCHAIN\r\nKMC S1, 0.5x0.125 inches\r\nREAR COGS\r\n16 T\r\nHANDLEBAR\r\nLightweight Alloy, 500mm wide, 22.2mm\r\nGRIPS\r\nCannondale Kid Comfort\r\nSTEM\r\nAlloy Ahead, 37mm\r\nHEADSET\r\n1 inches Ahead\r\nBRAKES\r\nRear coaster brake\r\nSADDLE\r\nCannondale Kid Comfort\r\nSEATPOST\r\nOne-piece, clamp-less, 25.4mm\r\nSIZES\r\nOne Size\r\n', 3, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_bicycle_kid_bike_02.png', 0, '1', '1602699551'),
(65, 'CANNONDALE TRAIL 6 MTB BIKE', 39499, 200, 'FRAME\r\nNew Trail, SmartForm C2 Alloy, SAVE, BOOST spacing, Tapered Headtube, Flat Mount Rear Brake, Internal Cable Routing.\r\nFORK\r\nSR Suntour XCM-RL, 100mm, remote lockout, 1.125 inches, 29 inches or 27.5 inches, Coil\r\nRIMS\r\nCannondale DC 5.0, double wall, 32-hole\r\nHUBS\r\nFormula DC1420 front, Custom BOOST QR Formula rear\r\nSPOKES\r\nStainless Steel, 14g\r\nTIRES\r\nWTB Ranger Comp 27.5 or 29x2.25 inches DNA Compound\r\nPEDALS\r\nCannondale Platform\r\nCRANK\r\nFSA Alpha Drive, forged, 22 or 36\r\nBOTTOM BRACKET\r\nTange LN 3912, square taper\r\nCHAIN\r\nKMC X9, 9-speed\r\nREAR COGS\r\nSunRace 11-36 9-speed\r\nFRONT DERAILLEUR\r\nMicroShift Direct Mount\r\nREAR DERAILLEUR\r\nShimano Acera Shadow 9-speed\r\nSHIFTERS\r\nShimano Altus 2x9\r\nHANDLEBAR\r\nCannondale C4, 6061 Alloy, 20mm rise, 680mm\r\nGRIPS\r\nCannondale Dual-Density\r\nSTEM\r\nCannondale C4, 6061 Alloy, 31.8, 7A degrees\r\nHEADSET\r\nSealed Semi Integrated, 1.125 reducer\r\nBRAKES\r\nShimano M315 hydro disc, 160 or 160mm\r\nBRAKE LEVERS\r\nShimano M315 hydro disc\r\nSADDLE\r\nCannondale Stage 3\r\nSEATPOST\r\nCannondale C4, 6061 alloy, 31.6x350mm\r\n', 3, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_4.Cannondale Trail 6 MTB Bike.jpg', 0, '1', '1602700330'),
(66, 'CANNONDALE TRAIL 6 MTB BIKE 2019', 19999, 200, 'FRAME\r\nTrail, SmartForm C2 Alloy, SAVE, 1.125 inches head tube\r\nFORK\r\nSR Suntour XCM-RL, 100mm, hydraulic cable actuated lockout, Coil, 51mm Offset\r\nRIMS\r\nWTB SX19, 32h\r\nHUBS\r\nFormula QR front, Boost QR rear with HG driver\r\nSPOKES\r\nStainless Steel, 14g\r\nTIRES\r\nWTB Ranger Comp, 27.5 or 29 x 2.25 inches DNA Compound\r\nPEDALS\r\nCannondale Platform\r\nCRANK\r\nFSA Comet, Alpha Drive, 36 or 22\r\nBOTTOM BRACKET\r\nSealed Bearing BSA\r\nCHAIN\r\nKMC X9, 9-speed\r\nREAR COGS\r\nSunrace, 11-36, 9-speed\r\nFRONT DERAILLEUR\r\nMicroShift Direct Mount\r\nREAR DERAILLEUR\r\nShimano Acera\r\nSHIFTERS\r\nShimano Altus, 2x9\r\nHANDLEBAR\r\nCannondale C4 Riser, 6061 Alloy, 25mm rise, 8 degrees sweep, 6 degrees rise, 720mm\r\nGRIPS\r\nCannondale Dual-Density\r\nSTEM\r\nCannondale C4, 3D Forged 6061 Alloy, 1.125 inches, 31.8, 7 degrees\r\nHEADSET\r\nSealed Semi Integrated, 1.125 reducer\r\nBRAKES\r\nShimano MT200 hydro disc, 160 or 160mm RT26 rotors\r\nBRAKE LEVERS\r\nShimano MT200 hydro disc\r\nSADDLE\r\nCannondale Stage 3\r\nSEATPOST\r\nCannondale C4, 6061 Alloy, 31.6 x 400mm\r\nSIZES\r\nXS, S, M (27.5 inches), M (29 inches) \r\n', 3, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_5.Cannondale Trail 7 MTB Bike 2020.jpg', 0, '1', '1602700376'),
(67, 'CANNONDALE SUPERSIX EVO 105 DISC ROAD BIKE 2020', 59999, 200, 'FRAME All-New, BallisTec Carbon, integrated cable routing with \r\nSwitchplate, 142x12 Speed Release thru-axle, SAVE, \r\nPF30a, flat mount disc, integrated seat binder\r\nFORK All-New, BallisTec Carbon, SAVE, 1.125 inches to 1.25 inches steerer, \r\nintegrated crown race, 12x100mm Speed Release thru-axle, \r\nflat mount disc, internal routing, 55mm offset (47-54cm) 45mm offset (56-62cm)\r\nRIMS Fulcrum Racing 900 DB\r\nHUBS (F) Fulcrum Racing 900 DB, 12x100  or  (R) Fulcrum Racing 900 DB, 12x142\r\nSPOKES Fulcrum, Stainless steel\r\nTIRES Vittoria Zaffiro Pro Slick, 700 x 25c\r\nPEDALS Not Included\r\nCRANK Cannondale 1, BB30a, FSA rings, 50 or 34\r\nBOTTOM BRACKET Cannondale Alloy PressFit30\r\nCHAIN Shimano HG601, 11-speed\r\nREAR COGS Shimano 105, 11-30, 11-speed\r\nFRONT DERAILLEUR Shimano 105, braze-on\r\nREAR DERAILLEUR Shimano 105 GS\r\nSHIFTERS Shimano 105 hydro disc, 2x11\r\nHANDLEBAR Cannondale 3, 6061 alloy, Compact\r\nGRIPS Cannondale Grip Bar Tape withGel, 3.5mm\r\nSTEM Cannondale 3, 6061 alloy, 7-degree\r\nHEADSET SuperSix, 1.25 inches lower bearing, 25mm top cap\r\nBRAKES Shimano 105 hydro disc, 160 or 160mm RT54 rotors\r\nBRAKE LEVERS Shimano 105 hydro disc\r\nSADDLE Prologo Nago RS STN\r\nSEATPOST All-New HollowGram 27 KNOT, Alloy, 2 bolt clamp, 330mm\r\nSIZES 44, 48, 51\r\n', 2, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_9.Cannondale SuperSix Evo 105 Disc Road Bike 2020.jpg', 0, '1', '1602700673'),
(68, 'CANNONDALE SUPERSIX EVO 105 DISC ROAD BIKE 2020', 59999, 200, 'FRAME All-New, BallisTec Carbon, integrated cable routing with \r\nSwitchplate, 142x12 Speed Release thru-axle, SAVE, \r\nPF30a, flat mount disc, integrated seat binder\r\nFORK All-New, BallisTec Carbon, SAVE, 1.125 inches to 1.25 inches steerer, \r\nintegrated crown race, 12x100mm Speed Release thru-axle, \r\nflat mount disc, internal routing, 55mm offset (47-54cm) 45mm offset (56-62cm)\r\nRIMS Fulcrum Racing 900 DB\r\nHUBS (F) Fulcrum Racing 900 DB, 12x100  or  (R) Fulcrum Racing 900 DB, 12x142\r\nSPOKES Fulcrum, Stainless steel\r\nTIRES Vittoria Zaffiro Pro Slick, 700 x 25c\r\nPEDALS Not Included\r\nCRANK Cannondale 1, BB30a, FSA rings, 50 or 34\r\nBOTTOM BRACKET Cannondale Alloy PressFit30\r\nCHAIN Shimano HG601, 11-speed\r\nREAR COGS Shimano 105, 11-30, 11-speed\r\nFRONT DERAILLEUR Shimano 105, braze-on\r\nREAR DERAILLEUR Shimano 105 GS\r\nSHIFTERS Shimano 105 hydro disc, 2x11\r\nHANDLEBAR Cannondale 3, 6061 alloy, Compact\r\nGRIPS Cannondale Grip Bar Tape withGel, 3.5mm\r\nSTEM Cannondale 3, 6061 alloy, 7-degree\r\nHEADSET SuperSix, 1.25 inches lower bearing, 25mm top cap\r\nBRAKES Shimano 105 hydro disc, 160 or 160mm RT54 rotors\r\nBRAKE LEVERS Shimano 105 hydro disc\r\nSADDLE Prologo Nago RS STN\r\nSEATPOST All-New HollowGram 27 KNOT, Alloy, 2 bolt clamp, 330mm\r\nSIZES 44, 48, 51\r\n', 2, 'https://new-cycle-bike-shop.000webhostapp.com/resources/image/IMG_9.Cannondale SuperSix Evo 105 Disc Road Bike 2020.jpg', 0, '1', '1602735593');

-- --------------------------------------------------------

--
-- Table structure for table `ratings_reviews`
--

CREATE TABLE `ratings_reviews` (
  `id` int(11) NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `product_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL DEFAULT 0,
  `review` text NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `phone_no` varchar(255) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `balance` int(11) DEFAULT 0,
  `joined` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_admin_user_id` (`user_id`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_cart_product_product_id` (`product_id`),
  ADD KEY `fk_cart_user_user_id` (`user_id`);

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_order_product_product_id` (`product_id`),
  ADD KEY `fk_order_user_user_id` (`user_id`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ratings_reviews`
--
ALTER TABLE `ratings_reviews`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_rr_product_product_id` (`product_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `login`
--
ALTER TABLE `login`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `fk_admin_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `fk_cart_product_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_cart_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `fk_order_product_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_order_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ratings_reviews`
--
ALTER TABLE `ratings_reviews`
  ADD CONSTRAINT `fk_rr_product_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
